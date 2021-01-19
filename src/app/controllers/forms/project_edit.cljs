(ns app.controllers.forms.project-edit
  (:require [keechma.next.controller :as ctrl]
            [keechma.next.controllers.pipelines :as pipelines]
            [keechma.pipelines.core :as pp :refer-macros [pipeline!]]
            [keechma.next.controllers.form :as form]
            [keechma.next.controllers.router :as router]
            [app.gql :refer [m!]]
            [app.validators :as v]))

(derive :project-edit-form ::pipelines/controller)

(def pipelines
  {:keechma.form/get-data    (pipeline! [_ {:keys [deps-state*]}]
                                        {:name        (get-in @deps-state* [:router :name])
                                         :description (get-in @deps-state* [:router :description])
                                         :startDate   (get-in @deps-state* [:router :startDate])
                                         :endDate     (get-in @deps-state* [:router :endDate])})

   :delete-project            (pipeline! [value {:keys [deps-state*] :as ctrl}]
                                        (m! [:delete-project [:deleteProject]] {:id (get-in @deps-state* [:router :id])})
                                        (router/redirect! ctrl :router {:page "projekt"}))

   :keechma.form/submit-data (pipeline! [value {:keys [deps-state*] :as ctrl}]
                                        (m! [:update-project [:updateProject]] {:id          (get-in @deps-state* [:router :id])
                                                                                :name        (:name value)
                                                                                :description (:description value)
                                                                                :startDate   (:startDate value)
                                                                                :endDate     (:endDate value)})
                                        (router/redirect! ctrl :router {:page "projekt"}))
   })

(defmethod ctrl/prep :project-edit-form [ctrl]
           (pipelines/register ctrl
                               (form/wrap pipelines
                                          (v/to-validator {:name        [:not-empty]
                                                           :description [:not-empty]
                                                           :startDate   [:not-empty]
                                                           :endDate     [:not-empty]}))))
