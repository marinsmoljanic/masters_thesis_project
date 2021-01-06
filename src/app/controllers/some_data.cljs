(ns app.controllers.some-data
  (:require [keechma.next.controller :as ctrl]
            [app.gql :refer [q!]]))

(derive :some-data :keechma/controller)

(defmethod ctrl/start :some-data [_ _ _ _]
  #_(println "-=-=-=-=-=-=-=-=-=-=-=->" (q! [:roles [:allRole]] {}))
  [{:id 1
    :name "One"}
   {:id 2
    :name "Two"}
   {:id 3
    :name "Three"}
   {:id 4
    :name "Four with space"}])

(defmethod ctrl/handle :some-data [{:keys [state*]} event payload]
  (case event
    :update-name (let [new-state (mapv (fn [item]
                                         (if (= (:id item) (:id payload))
                                           payload
                                           item)) @state*)]
                   (reset! state* new-state))
    nil))



#_(def load-data
    (-> (pipeline! [value {:keys [deps-state*] :as ctrl}]
                   (q! [:roles []] {})
                   (edb/insert-named! ctrl :entitydb :rolestype :roles-data value))
        (pp/set-queue :load-roles)))

#_(def pipelines
    {:keechma.on/start load-roles})

#_(defmethod ctrl/prep :roles [ctrl]
             (pipelines/register ctrl pipelines))





(defmethod ctrl/derive-state :some-data [_ state _]
           (let [_ (println "")]
                state))