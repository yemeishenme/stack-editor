
(ns stack-editor.updater.collection)

(defn add-definition [store op-data]
  (let [definition-path op-data
        path [:collection :definitions definition-path]
        maybe-definition (get-in store path)]
    (if (some? maybe-definition) store (assoc-in store path []))))

(defn set-main [store op-data]
  (let [main-definition op-data]
    (assoc-in store [:collection :main-definition] main-definition)))

(defn edit [store op-data]
  (let [definition-path op-data]
    (-> store
     (update
       :writer
       (fn [writer]
         (merge
           writer
           {:kind :definitions, :stack [definition-path], :focus []})))
     (assoc :router {:name :workspace, :data nil}))))
