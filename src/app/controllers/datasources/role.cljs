(ns app.controllers.datasources.role
  (:require [app.gql :refer [q!]]
            [keechma.pipelines.core :as pp :refer-macros [pipeline!]]

            [keechma.next.controller :as ctrl]
            [keechma.next.controllers.entitydb :as edb]
            [keechma.next.controllers.pipelines :as pipelines]))

(derive :roles ::pipelines/controller)

(def load-roles
  (-> (pipeline! [value {:keys [deps-state*] :as ctrl}]
                 (q! [:roles [:allRole]] {})
                 (edb/insert-collection! ctrl :entitydb :role :role/list value))
      (pp/set-queue :load-roles)))

(def pipelines
  {:keechma.on/start load-roles})

(defmethod ctrl/prep :roles [ctrl]
  (pipelines/register ctrl pipelines))

(defmethod ctrl/derive-state :roles [_ state {:keys [entitydb]}]
   (let [roles (edb/get-collection entitydb :role/list)]
         (edb/get-collection entitydb :role/list)))