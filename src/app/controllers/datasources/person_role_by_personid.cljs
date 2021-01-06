(ns app.controllers.datasources.person-role-by-personid
  (:require [app.gql :refer [q!]]
            [keechma.pipelines.core :as pp :refer-macros [pipeline!]]
            [keechma.next.toolbox.logging :as l]

            [keechma.next.controller :as ctrl]
            [keechma.next.controllers.entitydb :as edb]
            [keechma.next.controllers.pipelines :as pipelines]))

(derive :person-role-by-personid ::pipelines/controller)

(defn filter-person-roles-by-personid [person-roles person-id]
      (filterv (fn [person-role]
               (= person-id (:PersonId person-role)))
               person-roles)
      )

(defn get-role-name-by-roleid [roles role-id]
      (get-in (first (filterv (fn [role] (= role-id (:id role))) roles)) [:Name]))

(defn enrich-person-roles [person-roles roles]
      (map (fn [person-role]
               (assoc person-role :roleName (get-role-name-by-roleid roles (:RoleId person-role))))
           person-roles))

(def load-person-role
  (-> (pipeline! [value {:keys [deps-state*] :as ctrl}]
                 (q! [:person-roles [:allPersonRole]] {})
                 (let [roles (get-in (:roles @deps-state*) [:allRole])
                       person-id (str (get-in @deps-state* [:router :id]))
                       person-roles (filter-person-roles-by-personid value person-id)
                       data-enriched-person-roles (enrich-person-roles person-roles roles)]
                      (pipeline! [value {:keys [deps-state*] :as ctrl}])
                            (l/pp "Enrhchani person rolsi: " data-enriched-person-roles)
                            (println person-id)
                            (l/pp roles)
                            (l/pp "---------------------------------------------------------------------")
                      )
                 #_(q! [:person-role-by-person-id []] {:personid (str (get-in @deps-state* [:router :id]))})
                 #_(edb/insert-named! ctrl :entitydb :personrolestype :person-role-by-personid value))
      (pp/set-queue :load-person-role-by-personid)))

(def pipelines
  {:keechma.on/start load-person-role})

(defmethod ctrl/prep :person-role-by-personid [ctrl]
           (pipelines/register ctrl pipelines))

(defmethod ctrl/derive-state :person-role-by-personid [_ state {:keys [entitydb]}]
           (edb/get-named entitydb :person-role-by-personid))


