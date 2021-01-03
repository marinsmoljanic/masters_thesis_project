(ns app.controllers.forms.project
  (:require [keechma.next.controller :as ctrl]
            [keechma.next.controllers.pipelines :as pipelines]
            [keechma.pipelines.core :as pp :refer-macros [pipeline!]]
            [keechma.next.controllers.form :as form]
            [keechma.next.controllers.router :as router]
            [app.gql :refer [m!]]
            [app.validators :as v]))

(derive :project-form ::pipelines/controller)

(def pipelines
  {:toggle                   (pipeline! [_ {:keys [state*]}]
                                        (pp/swap! state* update :is-project-form-open? not))
   :keechma.form/submit-data (pipeline! [value {:keys [state*] :as ctrl}]
                                        (println "OPAAAAA_________________________________-")
                                        (m! [:create-project [:createProject]] {:name        (:name value)
                                                                               :description (:description value)
                                                                               :startDate   (:startDate value)
                                                                               :endDate     (:endDate value)})
                                        (pp/swap! state* update :is-project-form-open? not)
                                        (router/redirect! ctrl :router {:page ""}))})

(defmethod ctrl/start :project-form [_ state _ _]
           {:is-project-form-open? nil})

(defmethod ctrl/prep :project-form [ctrl]
           (pipelines/register ctrl
                               (form/wrap pipelines
                                          (v/to-validator {:name     [:not-empty]
                                                           :description [:not-empty]
                                                           :startDate  [:not-empty]
                                                           :endDate    [:not-empty]}))))
