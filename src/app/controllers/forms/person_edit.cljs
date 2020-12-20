(ns app.controllers.forms.person-edit
  (:require [keechma.next.controller :as ctrl]
            [keechma.next.controllers.pipelines :as pipelines]
            [keechma.pipelines.core :as pp :refer-macros [pipeline!]]
            [keechma.next.controllers.form :as form]
            [keechma.next.controllers.router :as router]
            [app.gql :refer [m!]]
            [app.validators :as v]))

(derive :person-edit-form ::pipelines/controller)

(def pipelines
  {:keechma.form/get-data (pipeline! [_ {:keys [deps-state*]}]
                                     (println "")
                                       {:firstName (get-in @deps-state* [:router :id])
                                        :lastName  "Smoljanic"})

   :keechma.form/submit-data (pipeline! [value ctrl]
                                        #_(m! [:login [:login :token]] {:input value})
                                        #_(ctrl/broadcast ctrl :anon/login value)
                                        (router/redirect! ctrl :router {:page "osoba"}))})

(defmethod ctrl/start :person-edit-form [_ state _ _]
  {:is-form-open? nil})

(defmethod ctrl/prep :person-edit-form [ctrl]
  (pipelines/register ctrl
                      (form/wrap pipelines
                                 (v/to-validator {:ime     [:not-empty]
                                                  :prezime [:not-empty]}))))
