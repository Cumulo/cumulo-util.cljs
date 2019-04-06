
(ns cumulo-util.app
  (:require [cumulo-util.core :refer [delay!]]
            [clojure.core.async :refer [go chan >! <!]]
            [cumulo-util.async :refer [all-once]]))

(defn task! []
  (go
   (let [chan-rand-sleep (fn []
                           (let [<result (chan), x (rand-int 10)]
                             (delay!
                              x
                              (fn [] (go (println "finished task" x) (>! <result x))))
                             <result))
         all-results (<! (all-once chan-rand-sleep (repeat (rand-int 10) true)))]
     (println "all results:" all-results))))

(defn main! [] (println "Started") (task!))

(defn reload! [] (println "Reload") (task!))
