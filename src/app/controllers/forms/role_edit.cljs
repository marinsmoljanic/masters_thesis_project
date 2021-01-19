(ns app.controllers.forms.role-edit
  (:require [keechma.next.controller :as ctrl]
            [keechma.next.controllers.pipelines :as pipelines]
            [keechma.pipelines.core :as pp :refer-macros [pipeline!]]
            [keechma.next.controllers.form :as form]
            [keechma.next.controllers.router :as router]
            [keechma.next.controllers.entitydb :as edb]
            [app.gql :refer [m! q!]]
            [app.validators :as v]))

(derive :role-edit-form ::pipelines/controller)

(def pipelines
  {:keechma.form/get-data    (pipeline! [value {:keys [deps-state*] :as ctrl}]
                                        value)

   :keechma.form/submit-data (pipeline! [value {:keys [deps-state*] :as ctrl}]
                                        (m! [:update-role [:updateRole]] {:id (:id value)
                                                                          :name (:Name value)})
                                        (q! [:roles [:allRole]] {})
                                        (edb/insert-collection! ctrl :entitydb :role :role/list value))

   :delete                   (pipeline! [value {:keys [deps-state*] :as ctrl}]
                                        (m! [:delete-role [:deleteRole]] {:id value})
                                        (q! [:roles [:allRole]] {})
                                        (edb/insert-collection! ctrl :entitydb :role :role/list value))})

(defmethod ctrl/prep :role-edit-form [ctrl]
           (pipelines/register ctrl
                               (form/wrap pipelines
                                          (v/to-validator {}))))
