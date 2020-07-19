
(ns cumulo-util.app
  (:require [cumulo-util.core :refer [delay!]]
            [clojure.core.async :refer [go chan >! <!]]
            [cumulo-util.async :refer [all-once]]
            [cumulo-util.file :refer [chan-pick-port write-mildly!]]
            [clojure.core.async :refer [go <! chan timeout]]
            [cumulo-util.build-info :refer [on-build!]]))

(defn pick-port! [] (go (let [port (<! (chan-pick-port 6001))] (println "got port" port))))

(defn wait-sleep! []
  (go
   (let [chan-rand-sleep (fn []
                           (go
                            (let [duration (rand-int 10)]
                              (println "rand value:" duration)
                              (<! (timeout (* 1000 duration)))
                              (println "finished task" duration)
                              duration)))
         all-results (<! (all-once chan-rand-sleep (repeat (rand-int 10) true)))]
     (println "all results:" all-results))))

(defn task! [] (comment wait-sleep!))

(defn main! []
  (println "Started")
  (task!)
  (pick-port!)
  (comment write-mildly! "a/a/a" "a")
  (comment wait-sleep!))

(defn reload! [] (println "Reload") (task!))
