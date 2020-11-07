(ns app.gql
  (:require [graphql-builder.parser :refer-macros [defgraphql]]
            [graphql-builder.core :as core]
            [keechma.next.toolbox.logging :as l]
            [keechma.next.toolbox.ajax :refer [POST]]
            [ajax.core :refer [to-interceptor]]
            [oops.core :refer [ocall oget]]
            [promesa.core :as p]
            [app.domain.settings :refer [gql-endpoint]]
            [clojure.string :as str]))


(defn get-mutation [query-map name] (get-in query-map [:mutation name]))

(defn get-query [query-map name] (get-in query-map [:query name]))

(defgraphql queries "resources/graphql/queries.graphql")

(def q-map
  (try (core/query-map queries {:inline-fragments true})
       (catch js/Object _ (throw queries))))

(defn log-success [req] (l/log "REQ" req) req)
(defn log-error [err] (throw err))

(defn log-as-curl
  [url headers data]
  (str "curl -X POST '"
       url
       "' -H 'Content-Type: application/json' "
       (str/join " "
                 (map (fn [[k v]] (str " -H '" (name k) ": " v "'")) headers))
       " --data-binary '"
       (js/JSON.stringify (clj->js data))
       "' --compressed"))

(defn log-graphql-info
  [graphql sent-headers]
  (l/group-collapsed "Request")
  (l/log "Operation Name:" (:operationName graphql))
  (l/log (:query graphql))
  (l/pp "Variables:" (:variables graphql))
  (l/log (log-as-curl gql-endpoint sent-headers graphql))
  (l/group-end))


(defn make-logger-interceptor
  [graphql]
  (let [sent-headers$ (atom nil)]
    (->
      {:name    :logger
       :request (fn [request] (reset! sent-headers$ (:headers request)) request)
       :response
                (fn [response]
                  (if-let [error (not= 0 (ocall response :getLastErrorCode))]
                    (do (l/group "%cGraphQL Error" "color:red; font-size: 14px;")
                        (l/error (ocall response :getStatus) error)
                        (l/log (ocall response :getResponseJson))
                        (log-graphql-info graphql @sent-headers$)
                        (l/group-end))
                    (do (l/group-collapsed "%cGraphQL Success" "color:green;"
                                           " -> " (:operationName graphql))
                        (log-graphql-info graphql @sent-headers$)
                        (l/group-collapsed "Response")
                        (try (l/pp
                               (js->clj (oget (ocall response :getResponseJson) :?data)
                                        :keywordize-keys
                                        true))
                             (catch :default _ (l/pp "Unprocessable entity")))
                        (l/group-end)
                        (l/group-end)))
                  response)}
      to-interceptor)))

(defn add-authentication-header
  [headers token]
  (if token (assoc headers :Authorization (str "Bearer " token)) headers))

(defn extract-gql-error [error] (oget (get-in error [:payload]) "data"))

(defn gql-results-handler
  [unpack]
  (fn [{:keys [data errors]}]
    (if errors (throw (ex-info "GraphQLError" errors)) (unpack data))))

(defn gql-req!
  ([query-type query-config] (gql-req! query-type query-config {} nil))
  ([query-type query-config variables]
   (gql-req! query-type query-config variables nil))
  ([query-type query-config variables token]
   (let [[query-name extract-path]
         (if (vector? query-config) query-config [query-config])]
     (if-let [query-fn (get-in q-map [query-type query-name])]
       (let [{:keys [graphql unpack]} (query-fn variables)]
         (when query-fn
           (->>
             (POST gql-endpoint
                   {:keywords?       true
                    :response-format :json
                    :format          :json
                    :params          (clj->js graphql)
                    :headers         (-> {}
                                         (add-authentication-header token))
                    :interceptors    [(make-logger-interceptor graphql)]})
             (p/map (gql-results-handler unpack))
             (p/map
               (fn [res]
                 (if extract-path (get-in res (flatten [extract-path])) res))))))
       (do (l/group "Missing Graphql Query")
           (l/warn "Couldn't find GraphQL query named"
                   (str query-name " (" query-type "). Available queries:"))
           (l/pp (keys (q-map query-type)))
           (l/group-end))))))

(defn keyname [key] (str (namespace key) "/" (name key)))

(def m! (partial gql-req! :mutation))
(def q! (partial gql-req! :query))