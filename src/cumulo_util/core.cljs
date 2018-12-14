
(ns cumulo-util.core (:require ["shortid" :as shortid]))

(defn delay! [duration task] (js/setTimeout task (* 1000 duration)))

(defn find-first [f xs] (reduce (fn [_ x] (when (f x) (reduced x))) nil xs))

(defn get-env! [property] (aget (.-env js/process) property))

(defn id! [] (.generate shortid))

(defn repeat! [duration task] (js/setInterval task (* 1000 duration)))

(defn unix-time! [] (.valueOf (js/Date.)))
