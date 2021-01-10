(ns app.controllers.datasources.person-role-by-personid
  (:require [app.gql :refer [q!]]
            [keechma.next.controller :as ctrl]
            [keechma.next.toolbox.logging :as l]
            [keechma.next.controllers.entitydb :as edb]
            [keechma.next.controllers.pipelines :as pipelines]
            [keechma.pipelines.core :as pp :refer-macros [pipeline!]]))

(derive :person-role-by-personid ::pipelines/controller)

(defn filter-person-roles-by-personid [person-roles person-id]
      (filterv (fn [person-role]
               (= person-id (:PersonId person-role)))
               person-roles))

(defn get-role-name-by-roleid [roles role-id]
      (get-in (first (filterv (fn [role] (= role-id (:id role))) roles)) [:Name]))

(defn get-project-name-by-projectid [projects project-id]
      (get-in (first (filterv (fn [project] (= project-id (:id project))) projects)) [:Name]))

(defn enrich-person-roles [person-roles roles projects]
      (map (fn [person-role]
               (assoc person-role :roleName    (get-role-name-by-roleid roles (:RoleId person-role))
                                  :projectName (get-project-name-by-projectid projects (:ProjectCode person-role))))
           person-roles))

(def load-person-role
  (-> (pipeline! [value {:keys [deps-state*] :as ctrl}]
                 (q! [:person-roles [:allPersonRole]] {})
                 (let [roles (:roles @deps-state*)
                       projects (get-in (:projects @deps-state*) [:allProject])
                       person-id (str (get-in @deps-state* [:router :id]))
                       person-roles (filter-person-roles-by-personid value person-id)
                       data-enriched-person-roles (vec (enrich-person-roles person-roles roles projects))]
                      (pipeline! [value {:keys [deps-state*] :as ctrl}]
                            (edb/insert-collection! ctrl :entitydb :person-roles-enriched :person-roles-enriched/list data-enriched-person-roles))))
      (pp/set-queue :load-person-role-by-personid)))

(def pipelines
  {:keechma.on/start load-person-role})

(defmethod ctrl/prep :person-role-by-personid [ctrl]
           (pipelines/register ctrl pipelines))

(defmethod ctrl/derive-state :person-role-by-personid [_ state {:keys [entitydb]}]
           (edb/get-collection entitydb :person-roles-enriched/list))


