(ns app.controllers.datasources.person-role-by-projectid
  (:require [app.gql :refer [q!]]
            [keechma.next.controller :as ctrl]
            [keechma.next.toolbox.logging :as l]
            [keechma.next.controllers.entitydb :as edb]
            [keechma.next.controllers.pipelines :as pipelines]
            [keechma.pipelines.core :as pp :refer-macros [pipeline!]]))

(derive :person-role-by-projectid ::pipelines/controller)

(defn filter-person-roles-by-projectid [person-roles project-id]
      (filterv (fn [person-role]
                   (= project-id (:ProjectCode person-role)))
               person-roles))

(defn get-role-name-by-roleid [roles role-id]
      (get-in (first (filterv (fn [role] (= role-id (:id role))) roles)) [:Name]))

(defn get-person-name-by-personid [persons person-id]
      (str
        (get-in (first (filterv (fn [person] (= person-id (:id person))) persons)) [:FirstName])
        " "
        (get-in (first (filterv (fn [person] (= person-id (:id person))) persons)) [:LastName])))

(defn enrich-person-roles [person-roles roles persons]
      (map (fn [person-role]
               (assoc person-role :roleName    (get-role-name-by-roleid roles (:RoleId person-role))
                                  :personName  (get-person-name-by-personid persons (:PersonId person-role))))
           person-roles))

(def load-person-role
  (-> (pipeline! [value {:keys [deps-state*] :as ctrl}]
                 (q! [:person-roles [:allPersonRole]] {})
                 (let [roles (:roles @deps-state*)
                       persons (get-in (:persons @deps-state*) [:allPerson])
                       project-id (str (get-in @deps-state* [:router :id]))
                       person-roles (filter-person-roles-by-projectid value project-id)
                       data-enriched-person-roles (vec (enrich-person-roles person-roles roles persons))]

                      (pipeline! [value {:keys [deps-state*] :as ctrl}]
                                 (edb/insert-collection! ctrl :entitydb :person-roles-enriched-by-project :person-roles-enriched-by-project/list data-enriched-person-roles))))
      (pp/set-queue :load-person-role-by-personid)))

(def pipelines
  {:keechma.on/start load-person-role})

(defmethod ctrl/prep :person-role-by-projectid [ctrl]
           (pipelines/register ctrl pipelines))

(defmethod ctrl/derive-state :person-role-by-projectid [_ state {:keys [entitydb]}]
           (edb/get-collection entitydb :person-roles-enriched-by-project/list))


