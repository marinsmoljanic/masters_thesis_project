(ns app.controllers.datasources.person-role-by-personid
  (:require [app.gql :refer [q!]]
            [keechma.pipelines.core :as pp :refer-macros [pipeline!]]

            [keechma.next.controller :as ctrl]
            [keechma.next.controllers.entitydb :as edb]
            [keechma.next.controllers.pipelines :as pipelines]))

(derive :person-role-by-personid ::pipelines/controller)

(def load-person-role
  (-> (pipeline! [value {:keys [deps-state*] :as ctrl}]
                 (q! [:person-role-by-person-id []] {:personid (str (get-in @deps-state* [:router :id]))})
                 (edb/insert-named! ctrl :entitydb :personrolestype :person-role-by-personid value))
      (pp/set-queue :load-person-role-by-personid)))

(def pipelines
  {:keechma.on/start load-person-role})

(defmethod ctrl/prep :person-role-by-personid [ctrl]
           (pipelines/register ctrl pipelines))

(defmethod ctrl/derive-state :person-role-by-personid [_ state {:keys [entitydb]}]
           (edb/get-named entitydb :person-role-by-personid))


