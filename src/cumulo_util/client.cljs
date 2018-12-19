
(ns cumulo-util.client (:require [cumulo-util.core :refer [on-page-touch]]))

(defn main! [] (on-page-touch (fn [] (println "called"))))

(defn reload! [] )
