
(ns cumulo-util.async (:require [clojure.core.async :refer [go >! <! chan]]))

(defn all-once [chan-f xs]
  (assert (fn? chan-f) "expected a function")
  (assert (sequential? xs) "expected a sequence")
  (let [all-tasks (doall (map chan-f xs)), <result (chan), *counter (atom 0)]
    (go
     (loop [acc [], tasks all-tasks]
       (if (empty? tasks)
         (>! <result acc)
         (do (swap! *counter inc) (recur (conj acc (<! (first tasks))) (rest tasks))))))
    <result))
