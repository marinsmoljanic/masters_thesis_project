(ns app.controllers.datasources.project
  (:require [app.gql :refer [q!]]
            [keechma.pipelines.core :as pp :refer-macros [pipeline!]]

            [keechma.next.controller :as ctrl]
            [keechma.next.controllers.entitydb :as edb]
            [keechma.next.controllers.pipelines :as pipelines]))

(derive :projects ::pipelines/controller)

(def load-projects
  (-> (pipeline! [value {:keys [deps-state*] :as ctrl}]
                 (q! [:projects []] {})
                 (edb/insert-named! ctrl :entitydb :projecttype :projects-data value))
      (pp/set-queue :load-projects)))

(def pipelines
  {:keechma.on/start load-projects})

(defmethod ctrl/prep :projects [ctrl]
  (pipelines/register ctrl pipelines))

(defmethod ctrl/derive-state :projects [_ state {:keys [entitydb]}]
  (edb/get-named entitydb :projects-data))


