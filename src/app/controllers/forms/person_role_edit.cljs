(ns app.controllers.forms.person-role-edit
  (:require [keechma.next.controller :as ctrl]
            [keechma.next.controllers.pipelines :as pipelines]
            [keechma.pipelines.core :as pp :refer-macros [pipeline!]]
            [keechma.next.controllers.form :as form]
            [keechma.next.controllers.router :as router]
            [app.gql :refer [m!]]
            [tick.alpha.api :as t]
            [app.validators :as v]))

(derive :person-role-edit-form ::pipelines/controller)

(def pipelines
  {:keechma.form/get-data (pipeline! [_ {:keys [deps-state*]}]
                                     {:project "(get-in @deps-state* [:router :project])"
                                      :role    (get-in @deps-state* [:router :role])
                                      :date    "1"
                                      })

   :keechma.form/submit-data (pipeline! [value ctrl]
                                        #_(println "Submit value ----> " value)
                                        #_(println (t/date "2000-01-01"))
                                        #_(println (t/date (:date value)))
                                        #_(println (t/now))

                                        #_(println (t/instant (t/offset-date-time (str (:date value) "T00:00:00+00:00"))))
                                        #_(println (t/format (tick.format/formatter "yyyy-MMM-dd") (t/date)))

                                        #_(println (t/millis (t/between (t/epoch) (t/instant (t/offset-date-time (str (:date value) "T00:00:00+00:00"))))))
                                        ;;(println (t/new-time 1609459200000))
                                        #_(println (t/int 1609459200000))
                                        (println (t/inst (t/new-duration 1609459200000 :millis)))

                                        (m! [:update-person-role [:updatePersonRole]] {:project (:project value)
                                                                                         :role    (:role value)
                                                                                         :person  (:person value)
                                                                                         :date    (t/millis (t/between (t/epoch) (t/instant (t/offset-date-time (str (:date value) "T00:00:00+00:00")))))})
                                        #_(router/redirect! ctrl :router {:page "osoba"}))})

(defmethod ctrl/start :person-role-edit-form [_ state _ _]
           {:is-form-open? nil})

(defmethod ctrl/prep :person-role-edit-form [ctrl]
           (pipelines/register ctrl
                               (form/wrap pipelines
                                          (v/to-validator {}))))
