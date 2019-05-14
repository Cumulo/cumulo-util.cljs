
(ns cumulo-util.app
  (:require [cumulo-util.core :refer [delay!]]
            [clojure.core.async :refer [go chan >! <!]]
            [cumulo-util.async :refer [all-once]]
            [cumulo-util.file :refer [chan-pick-port write-mildly!]]
            [clojure.core.async :refer [go <! chan]]))

(defn pick-port! [] (go (let [port (<! (chan-pick-port 6001))] (println "got port" port))))

(defn wait-sleep! []
  (go
   (let [chan-rand-sleep (fn []
                           (let [<result (chan), x (rand-int 10)]
                             (delay!
                              x
                              (fn [] (go (println "finished task" x) (>! <result x))))
                             <result))
         all-results (<! (all-once chan-rand-sleep (repeat (rand-int 10) true)))]
     (println "all results:" all-results))))

(defn task! [] (comment wait-sleep!))

(defn main! [] (println "Started") (task!) (comment pick-port!) (write-mildly! "a/a/a" "a"))

(defn reload! [] (println "Reload") (task!))
