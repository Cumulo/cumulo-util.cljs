
(ns cumulo-util.core (:require ["path" :as path] ["fs" :as fs] ["child_process" :as cp]))

(defn detect-then-write! [file-path content]
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

(defn find-first [f xs] (reduce (fn [_ x] (when (f x) (reduced x))) nil xs))

(defn get-env! [property] (aget (.-env js/process) property))
