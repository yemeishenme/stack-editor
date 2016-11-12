
(ns stack-editor.comp.stack
  (:require [hsl.core :refer [hsl]]
            [clojure.string :as string]
            [respo-ui.style :as ui]
            [cirru-editor.util.dom :refer [focus!]]
            [respo.alias :refer [create-comp div]]
            [respo.comp.text :refer [comp-text]]
            [respo.comp.space :refer [comp-space]]
            [respo.comp.debug :refer [comp-debug]]))

(defn on-click [pointer]
  (fn [e dispatch!]
    (let [event (:original-event e)
          command? (or (.-ctrlKey event) (.-metaKey event))
          shift? (.-shiftKey event)]
      (cond
        command? (dispatch! :stack/collapse pointer)
        shift? (do (.preventDefault event) (dispatch! :stack/shift pointer))
        :else (do (dispatch! :stack/point-to pointer) (focus!))))))

(def style-bright {:color (hsl 0 0 90)})

(def style-bar
  {:line-height 1.4,
   :color (hsl 0 0 60),
   :white-space "nowrap",
   :font-size 13,
   :cursor "pointer",
   :padding "4px 8px",
   :font-family "Source Code Pro,Menlo,monospace"})

(def style-ns
  {:line-height 1.4, :color (hsl 0 0 50), :font-size "11px", :font-family "Hind"})

(def style-container {:overflow "auto", :padding "16px 0 160px 0", :user-select :none})

(def style-ns-main
  {:line-height "36px",
   :color (hsl 0 0 60),
   :white-space "nowrap",
   :font-size "13px",
   :cursor "pointer",
   :padding "0 8px",
   :font-family "Hind"})

(defn render [stack pointer]
  (fn [state mutate!]
    (div
     {:style (merge ui/flex style-container)}
     (->> stack
          (map-indexed
           (fn [idx item]
             [idx
              (if (string/includes? (last item) "/")
                (let [[ns-part var-part] (string/split (last item) "/")]
                  (div
                   {:style style-bar, :event {:click (on-click idx)}}
                   (div {:style (if (= idx pointer) style-bright)} (comp-text var-part nil))
                   (div {:style style-ns} (comp-text ns-part nil))))
                (div
                 {:style (merge style-ns-main (if (= idx pointer) style-bright)),
                  :event {:click (on-click idx)}}
                 (comp-text (last item) nil)))]))))))

(def comp-stack (create-comp :stack render))
