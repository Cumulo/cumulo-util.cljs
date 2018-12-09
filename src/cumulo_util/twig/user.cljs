
(ns cumulo-util.twig.user (:require [recollect.twig :refer [deftwig]]))

(deftwig twig-user (user) (dissoc user :password))
