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
                                       {:firstName (get-in @deps-state* [:router :firstName])
                                        :lastName  (get-in @deps-state* [:router :lastName])})

   :delete-person (pipeline! [value {:keys [deps-state*] :as ctrl}]
                                        (m! [:delete-person [:deletePerson]] {:id (get-in @deps-state* [:router :id])})
                                        (router/redirect! ctrl :router {:page "osoba"}))

   :keechma.form/submit-data (pipeline! [value {:keys [deps-state*] :as ctrl}]
                                        (m! [:update-person [:updatePerson]] {:id        (get-in @deps-state* [:router :id])
                                                                              :firstName (:firstName value)
                                                                              :lastName  (:lastName  value)})
                                        (router/redirect! ctrl :router {:page "osoba"}))
   })

(defmethod ctrl/start :person-edit-form [_ state _ _]
  {:is-form-open? nil})

(defmethod ctrl/prep :person-edit-form [ctrl]
  (pipelines/register ctrl
                      (form/wrap pipelines
                                 (v/to-validator {:firstName  [:not-empty]
                                                  :lastName   [:not-empty]}))))
