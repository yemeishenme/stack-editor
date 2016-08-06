
(ns stack-editor.comp.stack
  (:require [hsl.core :refer [hsl]]
            [respo-ui.style :as ui]
            [respo.alias :refer [create-comp div]]
            [respo.comp.text :refer [comp-text]]
            [respo.comp.space :refer [comp-space]]))

(defn render [stack pointer] (fn [state mutate!] (div {})))

(def comp-stack (create-comp :stack render))
