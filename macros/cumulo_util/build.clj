
(ns cumulo-util.build
  (:require [clojure.string :as string])
  (:import (java.net InetAddress)))

(defmacro get-ip! []
  (let [my-ip (.getHostAddress (InetAddress/getLocalHost))]
    (println "Current IP address is:" my-ip)
    my-ip))

(defmacro inline-resource [resource-path]
  (slurp resource-path))
