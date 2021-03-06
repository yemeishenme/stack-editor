
(ns stack-editor.updater.core
  (:require [respo.cursor :refer [mutate]]
            [stack-editor.updater.router :as router]
            [stack-editor.updater.collection :as collection]
            [stack-editor.updater.notification :as notification]
            [stack-editor.updater.stack :as stack]
            [stack-editor.updater.modal :as modal]))

(defn default-handler [store op-data] store)

(defn updater [store op op-data op-id]
  (let [handler (case op
                  :states (fn [x] (update x :states (mutate op-data)))
                  :router/route router/route
                  :router/toggle-palette router/toggle-palette
                  :collection/add-definition collection/add-definition
                  :collection/add-namespace collection/add-namespace
                  :collection/edit collection/edit
                  :collection/edit-ns collection/edit-ns
                  :collection/write collection/write-code
                  :collection/load collection/load-remote
                  :collection/remove-this collection/remove-this
                  :collection/rename collection/rename
                  :collection/hydrate collection/hydrate
                  :notification/add-one notification/add-one
                  :notification/remove-one notification/remove-one
                  :notification/remove-since notification/remove-since
                  :stack/goto-definition stack/goto-definition
                  :stack/dependents stack/dependents
                  :stack/go-back stack/go-back
                  :stack/go-next stack/go-next
                  :stack/point-to stack/point-to
                  :stack/collapse stack/collapse
                  :stack/shift stack/shift-one
                  :modal/mould modal/mould
                  :modal/recycle modal/recycle
                  default-handler)]
    (handler store op-data op-id)))
