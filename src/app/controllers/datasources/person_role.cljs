(ns app.controllers.datasources.person-role
  (:require [app.gql :refer [q!]]
            [keechma.pipelines.core :as pp :refer-macros [pipeline!]]

            [keechma.next.controller :as ctrl]
            [keechma.next.controllers.entitydb :as edb]
            [keechma.next.controllers.pipelines :as pipelines]))

(derive :person-roles ::pipelines/controller)

(def load-person-roles
  (-> (pipeline! [value {:keys [deps-state*] :as ctrl}]
                 (q! [:person-roles []] {})
                 (edb/insert-named! ctrl :entitydb :personrolestype :person-roles-data value))
      (pp/set-queue :load-roles)))

(def pipelines
  {:keechma.on/start load-person-roles})

(defmethod ctrl/prep :person-roles [ctrl]
  (pipelines/register ctrl pipelines))

(defmethod ctrl/derive-state :person-roles [_ state {:keys [entitydb]}]
  (edb/get-named entitydb :person-roles-data))


