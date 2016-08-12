
(ns stack-editor.util.detect
  (:require [clojure.string :as string]))

(defn fuzzy-search [pieces queries]
  (every?
    (fn [query]
      (some (fn [piece] (string/includes? piece query)) pieces))
    queries))

(defn strip-atom [token]
  (if (string/starts-with? token "@") (subs token 1) token))
