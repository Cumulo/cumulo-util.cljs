
(ns cumulo-util.async (:require [clojure.core.async :refer [go >! <! chan]]))

(defn all-once [chan-f xs]
  (assert (fn? chan-f) "expected a function")
  (assert (sequential? xs) "expected a sequence")
  (go
   (loop [acc [], tasks (doall (map chan-f xs))]
     (if (empty? tasks) acc (recur (conj acc (<! (first tasks))) (rest tasks))))))
