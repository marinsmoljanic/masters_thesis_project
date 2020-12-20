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
                                        (println "Came here")
                                        (pp/swap! state* update :is-project-form-open? not))
   :keechma.form/submit-data (pipeline! [value ctrl]
                                        #_(m! [:login [:login :token]] {:input value})
                                        #_(ctrl/broadcast ctrl :anon/login value)
                                        (router/redirect! ctrl :router {:page "osoba"}))})

(defmethod ctrl/start :project-form [_ state _ _]
           {:is-project-form-open? nil})

(defmethod ctrl/prep :project-form [ctrl]
           (pipelines/register ctrl
                               (form/wrap pipelines
                                          (v/to-validator {:name     [:not-empty]
                                                           :description [:not-empty]
                                                           :startdate  [:not-empty]
                                                           :enddate    [:not-empty]}))))
