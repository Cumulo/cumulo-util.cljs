
(ns cumulo-util.client
  (:require [respo.core :refer [render! clear-cache! realize-ssr!]]
            [respo.cursor :refer [mutate]]
            [cumulo-util.comp.container :refer [comp-container]]
            [cljs.reader :refer [read-string]]
            [cumulo-util.connection :refer [send! setup-socket!]]
            [cumulo-util.schema :as schema]
            [cumulo-util.config :as config]))

(declare dispatch!)

(declare connect!)

(declare simulate-login!)

(defonce *states (atom {}))

(defonce *store (atom nil))

(defn simulate-login! []
  (let [raw (.getItem js/localStorage (:storage-key config/site))]
    (if (some? raw)
      (do (println "Found storage.") (dispatch! :user/log-in (read-string raw)))
      (do (println "Found no storage.")))))

(defn dispatch! [op op-data]
  (println "Dispatch" op op-data)
  (case op
    :states (reset! *states ((mutate op-data) @*states))
    :effect/connect (connect!)
    (send! op op-data)))

(defn connect! []
  (setup-socket!
   *store
   {:url (str "ws://" (.-hostname js/location) ":" (:port config/site)),
    :on-close! (fn [event] (reset! *store nil) (.error js/console "Lost connection!")),
    :on-open! (fn [event] (simulate-login!))}))

(def mount-target (.querySelector js/document ".app"))

(defn render-app! [renderer]
  (renderer mount-target (comp-container @*states @*store) dispatch!))

(def ssr? (some? (.querySelector js/document "meta.respo-ssr")))

(defn main! []
  (if ssr? (render-app! realize-ssr!))
  (render-app! render!)
  (connect!)
  (add-watch *store :changes #(render-app! render!))
  (add-watch *states :changes #(render-app! render!))
  (.addEventListener
   js/window
   "visibilitychange"
   (fn [] (when (and (nil? @*store) (= "visible" js/document.visibilityState)) (connect!))))
  (println "App started!"))

(defn reload! [] (clear-cache!) (render-app! render!) (println "Code updated."))
