
(ns stack-editor.comp.namespaces
  (:require [hsl.core :refer [hsl]]
            [respo-ui.style :as ui]
            [respo-border.transform.space :refer [interpose-spaces]]
            [respo.alias :refer (create-comp div input)]
            [respo.comp.text :refer [comp-text]]
            [respo.comp.space :refer [comp-space]]
            [stack-editor.style.widget :as widget]
            [cirru-editor.util.dom :refer [focus!]]))

(defn init-state [& args] "")

(defn update-state [state text] text)

(defn on-input [mutate!] (fn [e dispatch!] (mutate! (:value e))))

(defn on-add-namespace [mutate! state]
  (fn [e dispatch!]
    (dispatch! :collection/add-namespace state)
    (mutate! "")))

(defn on-edit-ns [namespace']
  (fn [e dispatch!]
    (dispatch! :collection/edit [:namespaces namespace'])
    (focus!)))

(defn render [namespaces]
  (fn [state mutate!]
    (div
      {:style (merge ui/flex ui/card)}
      (div
        {}
        (input
          {:style widget/input,
           :event {:input (on-input mutate!)},
           :attrs {:placeholder "namespace", :value state}})
        (comp-space "8px" nil)
        (div
          {:style widget/button,
           :event {:click (on-add-namespace mutate! state)}}
          (comp-text "add" nil)))
      (comp-space nil "16px")
      (interpose-spaces
        (div
          {}
          (->>
            namespaces
            (sort-by first)
            (map
              (fn [entry]
                (let [ns-name (first entry)]
                  [ns-name
                   (div
                     {:style widget/entry-line,
                      :event {:click (on-edit-ns ns-name)}}
                     (comp-text ns-name nil))])))))
        {:height "8px"}))))

(def comp-namespaces
 (create-comp :namespaces init-state update-state render))
