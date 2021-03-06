
(ns stack-editor.actions
  (:require [cljs.reader :refer [read-string]]
            [ajax.core :refer [GET POST PATCH json-request-format]]
            [stack-editor.util.querystring :refer [parse-query]]
            [shallow-diff.diff :refer [diff]]))

(defonce remote-sepal-ref (atom nil))

(def options
  (merge {"port" "7010", "host" "localhost"} (parse-query (.-search js/location))))

(defn load-collection! [dispatch! open-analyzer?]
  (println (pr-str options))
  (GET
   (str "http://" (get options "host") ":" (get options "port"))
   {:handler (fn [response]
      (let [sepal-data (read-string response)]
        (if (not (contains? sepal-data :package)) (js/alert "Cannot find a :package field"))
        (dispatch! :collection/load sepal-data)
        (if open-analyzer? (dispatch! :router/route {:name :analyzer, :data nil}))
        (reset! remote-sepal-ref sepal-data))),
    :error-handler (fn [error]
      (println error)
      (dispatch! :notification/add-one "Failed to fetch collection"))}))

(defn submit-changes! [collection dispatch!]
  (PATCH
   (str "http://" (get options "host") ":" (get options "port"))
   {:format (json-request-format),
    :body (pr-str (diff @remote-sepal-ref collection)),
    :handler (fn [response]
      (println response)
      (dispatch! :notification/add-one "Patched")
      (reset! remote-sepal-ref collection)),
    :error-handler (fn [error]
      (println error)
      (if (zero? (:status error))
        (dispatch! :notification/add-one "Connection failed!")
        (let [response (read-string (:response error))]
          (dispatch! :notification/add-one (:status response)))))}))

(defn submit-collection! [collection dispatch!]
  (POST
   (str "http://" (get options "host") ":" (get options "port"))
   {:format (json-request-format),
    :body (pr-str collection),
    :handler (fn [response]
      (println response)
      (dispatch! :notification/add-one "Saved")
      (reset! remote-sepal-ref collection)),
    :error-handler (fn [error]
      (println error)
      (if (zero? (:status error))
        (dispatch! :notification/add-one "Connection failed!")
        (let [response (read-string (:response error))]
          (dispatch! :notification/add-one (:status response)))))}))

(defn display-code! [store]
  (let [writer (:writer store)
        collection (:collection store)
        path (concat (get (:stack writer) (:pointer writer)) (:focus writer))
        tree (get-in collection path)]
    (if (some? tree)
      (-> js/window (.open) (.-document) (.write "<pre><code>" (pr-str tree) "</code></pre>")))))
