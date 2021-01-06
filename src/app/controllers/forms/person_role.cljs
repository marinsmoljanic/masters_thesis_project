(ns app.controllers.forms.person-role
  (:require [keechma.next.controller :as ctrl]
            [keechma.next.controllers.pipelines :as pipelines]
            [keechma.pipelines.core :as pp :refer-macros [pipeline!]]
            [keechma.next.controllers.form :as form]
            [keechma.next.controllers.router :as router]
            [app.gql :refer [m!]]
            [app.validators :as v]))

(derive :person-role-form ::pipelines/controller)

(def pipelines
  {:keechma.form/submit-data (pipeline! [value ctrl]
                                        (println "Submit value: " value)
                                        (m! [:create-person-role [:createPersonRole]] {:project  (:project value)
                                                                                       :role     (:role value)
                                                                                       :person   (:person value)
                                                                                       :date     (:date value)})
                                        (router/redirect! ctrl :router {:page "osoba"}))})

(defmethod ctrl/start :person-role-form [_ state _ _]
           {:is-form-open? nil})

(defmethod ctrl/prep :person-role-form [ctrl]
           (pipelines/register ctrl
                               (form/wrap pipelines
                                          (v/to-validator {}))))
