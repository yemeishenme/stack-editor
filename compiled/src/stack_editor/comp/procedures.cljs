
(ns stack-editor.comp.procedures
  (:require [hsl.core :refer [hsl]]
            [respo.alias :refer [create-comp div]]))

(defn render [store] (fn [state mutate!] (div {})))

(def comp-procedures (create-comp :procedures render))