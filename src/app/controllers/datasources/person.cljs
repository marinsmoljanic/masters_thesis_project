(ns app.controllers.datasources.person
  (:require [app.gql :refer [q!]]
            [keechma.pipelines.core :as pp :refer-macros [pipeline!]]

            [keechma.next.controller :as ctrl]
            [keechma.next.controllers.entitydb :as edb]
            [keechma.next.controllers.pipelines :as pipelines]))

(derive :persons ::pipelines/controller)

(def load-persons
  (-> (pipeline! [value {:keys [deps-state*] :as ctrl}]
                 (q! [:persons []] {})
                 (edb/insert-named! ctrl :entitydb :persontype :persons-data value))
      (pp/set-queue :load-persons)))

(def pipelines
  {:keechma.on/start load-persons})

(defmethod ctrl/prep :persons [ctrl]
  (pipelines/register ctrl pipelines))

(defmethod ctrl/derive-state :persons [_ state {:keys [entitydb]}]
  (edb/get-named entitydb :persons-data))


