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
  {:keechma.form/get-data (pipeline! [_ {:keys [deps-state*]}]
                                     (println "")
                                     {:name (get-in @deps-state* [:router :id])
                                      :description  "Project description"
                                      :startdate    "1"
                                      :enddate      "2"})

   :keechma.form/submit-data (pipeline! [value ctrl]
                                        #_(m! [:login [:login :token]] {:input value})
                                        #_(ctrl/broadcast ctrl :anon/login value)
                                        (router/redirect! ctrl :router {:page "osoba"}))})

(defmethod ctrl/prep :project-edit-form [ctrl]
           (pipelines/register ctrl
                               (form/wrap pipelines
                                          (v/to-validator {:name        [:not-empty]
                                                           :description [:not-empty]
                                                           :startdate   [:not-empty]
                                                           :ednddate     [:not-empty]}))))
