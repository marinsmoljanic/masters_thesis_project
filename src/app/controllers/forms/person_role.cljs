(ns app.controllers.forms.person-role
  (:require [keechma.next.controller :as ctrl]
            [keechma.next.controllers.pipelines :as pipelines]
            [keechma.pipelines.core :as pp :refer-macros [pipeline!]]
            [keechma.next.controllers.form :as form]
            [keechma.next.controllers.router :as router]
            [app.gql :refer [m!]]
            [tick.alpha.api :as t]
            [app.validators :as v]))

(derive :person-role-form ::pipelines/controller)

(def pipelines
  {:keechma.form/submit-data (pipeline! [value ctrl]
                                        (println "Submit value ----> " value)
                                        (println (t/parse "2018-01-01"))
                                        (println (t/date "2000-01-01"))
                                        (println (t/millis (t/between (t/epoch) (t/now))))
                                        #_(m! [:create-person-role [:createPersonRole]] {:project (:project value)
                                                                                         :role    (:role value)
                                                                                         :person  (:person value)
                                                                                         :date    (:date value)})
                                        #_(router/redirect! ctrl :router {:page "osoba"})

                                        )})

(defmethod ctrl/start :person-role-form [_ state _ _]
           {:is-form-open? nil})

(defmethod ctrl/prep :person-role-form [ctrl]
           (pipelines/register ctrl
                               (form/wrap pipelines
                                          (v/to-validator {}))))
