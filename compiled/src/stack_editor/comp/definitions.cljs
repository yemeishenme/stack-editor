
(ns stack-editor.comp.definitions
  (:require [hsl.core :refer [hsl]]
            [respo.alias :refer (create-comp div)]))

(defn render [definitions] (fn [state mutate!] (div {})))

(def comp-definitions (create-comp :definitions render))
