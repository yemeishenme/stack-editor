
(ns stack-editor.updater.router)

(defn route [store op-data]
  (let [router op-data] (assoc store :router router)))
