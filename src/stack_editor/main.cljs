
(ns stack-editor.main
  (:require [respo.core
             :refer
             [render! clear-cache! render-element falsify-stage! gc-states!]]
            [stack-editor.schema :as schema]
            [stack-editor.comp.container :refer [comp-container]]
            [cljs.reader :refer [read-string]]
            [stack-editor.updater.core :refer [updater]]
            [stack-editor.util.keycode :as keycode]
            [stack-editor.util.dom :as dom]
            [stack-editor.util :refer [now!]]
            [stack-editor.actions
             :refer
             [load-collection! submit-collection! submit-changes!]]))

(defonce store-ref (atom schema/store))

(defn dispatch! [op op-data]
  (comment println "dispatch!" op op-data)
  (let [new-store (if (= op :effect/submit)
                    (let [[shift? collection] op-data]
                      (if shift?
                        (submit-collection! collection dispatch!)
                        (submit-changes! collection dispatch!))
                      @store-ref)
                    (updater @store-ref op op-data (now!)))]
    (reset! store-ref new-store)))

(defonce states-ref (atom {}))

(defn render-app! []
  (let [target (.querySelector js/document "#app")]
    (render! (comp-container @store-ref #{:dynamic :shell}) target dispatch! states-ref)))

(def ssr-stages
  (let [ssr-element (.querySelector js/document "#ssr-stages")
        ssr-markup (.getAttribute ssr-element "content")]
    (read-string ssr-markup)))

(defn -main! []
  (enable-console-print!)
  (if (not (empty? ssr-stages))
    (let [target (.querySelector js/document "#app")]
      (falsify-stage!
       target
       (render-element (comp-container @store-ref ssr-stages) states-ref)
       dispatch!)))
  (render-app!)
  (add-watch store-ref :gc (fn [] (gc-states! states-ref)))
  (add-watch store-ref :changes render-app!)
  (add-watch states-ref :changes render-app!)
  (.addEventListener
   js/window
   "keydown"
   (fn [event]
     (let [code (.-keyCode event)
           command? (or (.-metaKey event) (.-ctrlKey event))
           shift? (.-shiftKey event)]
       (cond
         (and command? (= code keycode/key-p))
           (do
            (.preventDefault event)
            (.stopPropagation event)
            (dispatch! :router/toggle-palette nil)
            (dom/focus-palette!))
         (and shift? command? (= code keycode/key-a))
           (do
            (let [router (:router @store-ref), writer (:writer @store-ref)]
              (if (= (:name router) :workspace)
                (dispatch! :router/route {:name :analyzer, :data :definitions})
                (if (not (empty? (:stack writer)))
                  (dispatch! :router/route {:name :workspace, :data nil})))))
         :else nil))))
  (println "app started!")
  (load-collection! dispatch!))

(defn on-jsload! [] (clear-cache!) (render-app!) (println "code updated."))

(set! js/window.onload -main!)
