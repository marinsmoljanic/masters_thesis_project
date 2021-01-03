(ns app.controllers.forms.person
  (:require [keechma.next.controller :as ctrl]
            [keechma.next.controllers.pipelines :as pipelines]
            [keechma.pipelines.core :as pp :refer-macros [pipeline!]]
            [keechma.next.controllers.form :as form]
            [keechma.next.controllers.router :as router]
            [app.gql :refer [m!]]
            [app.validators :as v]))

(derive :person-form ::pipelines/controller)

(def pipelines
  {:toggle                   (pipeline! [_ {:keys [state*]}]
                                        (pp/swap! state* update :is-form-open? not))
   :keechma.form/submit-data (pipeline! [value {:keys [state*] :as ctrl}]
                                        (m! [:create-person [:createPerson]] {:firstName (:firstName   value)
                                                                              :lastName  (:lastName    value)
                                                                              :personalId (:personalId value)})
                                        (pp/swap! state* update :is-form-open? not)
                                        (router/redirect! ctrl :router {:page ""}))})


(defmethod ctrl/start :person-form [_ state _ _]
  {:is-form-open? nil})

(defmethod ctrl/prep :person-form [ctrl]
  (pipelines/register ctrl
                      (form/wrap pipelines
                                 (v/to-validator {:firstName  [:not-empty]
                                                  :lastName   [:not-empty]
                                                  :personalId [:not-empty]}))))
