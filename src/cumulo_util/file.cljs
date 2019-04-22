
(ns cumulo-util.file
  (:require ["path" :as path]
            ["fs" :as fs]
            ["child_process" :as cp]
            ["net" :as net]
            [cljs.reader :refer [read-string]]
            [clojure.core.async :refer [go go-loop >! <! chan]])
  (:require-macros [clojure.core.strint :refer [<<]]))

(defn chan-port-taken [port]
  (let [<result (chan), tester (.createServer net)]
    (.. tester
        (once
         "error"
         (fn [err]
           (go
            (>!
             <result
             (if (not= (.-code err) "EADDRINUSE")
               {:error err, :data true}
               {:error nil, :data true})))))
        (once
         "listening"
         (fn []
           (.. tester
               (once "close" (fn [] (go (>! <result {:error nil, :data false}))))
               (close))))
        (listen port))
    <result))

(defn chan-pick-port [initial-port]
  (let [<result (chan)]
    (go-loop
     [port initial-port]
     (let [taken-info? (<! (chan-port-taken port))]
       (cond
         (some? (:error taken-info?))
           (do (js/console.error (:error taken-info?)) (js/process.exit 1))
         (:data taken-info?)
           (do (println (<< "port ~{port} is in use.")) (recur (inc port)))
         :else (>! <result port))))
    <result))

(defn get-backup-path! []
  (let [now (js/Date.)]
    (path/join
     js/__dirname
     "backups"
     (str (inc (.getMonth now)))
     (str (.getDate now) "-snapshot.edn"))))

(defn merge-local-edn! [x0 filepath handler]
  (merge
   x0
   (let [found? (fs/existsSync filepath)]
     (if (fn? handler) (handler found?))
     (if found? (read-string (fs/readFileSync filepath "utf8")) nil))))

(defn sh! [command] (println command) (println (.toString (cp/execSync command))))

(defn write-mildly! [file-path content]
  (let [do-write! (fn []
                    (cp/execSync (str "mkdir -p " (path/dirname file-path)))
                    (fs/writeFileSync file-path content)
                    (println "Write to file:" file-path))]
    (if (fs/existsSync file-path)
      (let [old-content (fs/readFileSync file-path "utf8")]
        (if (not= content old-content)
          (do-write!)
          (comment println "same file, skipping:" file-path)))
      (do-write!))))
