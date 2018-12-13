
(ns cumulo-util.file
  (:require ["path" :as path]
            ["fs" :as fs]
            ["child_process" :as cp]
            [cljs.reader :refer [read-string]]))

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
