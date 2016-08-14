
(ns stack-editor.updater.core
  (:require [stack-editor.updater.router :as router]
            [stack-editor.updater.collection :as collection]
            [stack-editor.updater.notification :as notification]
            [stack-editor.updater.stack :as stack]))

(defn default-handler [store op-data] store)

(defn updater [store op op-data op-id]
  (let [handler (case
                  op
                  :router/route
                  router/route
                  :router/toggle-palette
                  router/toggle-palette
                  :collection/add-definition
                  collection/add-definition
                  :collection/add-procedure
                  collection/add-procedure
                  :collection/add-namespace
                  collection/add-namespace
                  :collection/set-main
                  collection/set-main
                  :collection/edit
                  collection/edit
                  :collection/edit-ns
                  collection/edit-ns
                  :collection/write
                  collection/write-code
                  :collection/load
                  collection/load-remote
                  :collection/remove-this
                  collection/remove-this
                  :notification/add-one
                  notification/add-one
                  :notification/remove-one
                  notification/remove-one
                  :stack/goto-definition
                  stack/goto-definition
                  :stack/go-back
                  stack/go-back
                  :stack/point-to
                  stack/point-to
                  :stack/collapse
                  stack/collapse
                  default-handler)]
    (handler store op-data op-id)))
