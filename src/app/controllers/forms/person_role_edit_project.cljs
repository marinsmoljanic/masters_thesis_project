(ns app.controllers.forms.person-role-edit-project
  (:require [keechma.next.controller :as ctrl]
            [keechma.next.controllers.pipelines :as pipelines]
            [keechma.pipelines.core :as pp :refer-macros [pipeline!]]
            [keechma.next.controllers.form :as form]
            [keechma.next.controllers.router :as router]
            [app.gql :refer [m!]]
            [tick.alpha.api :as t]
            [app.validators :as v]))

(derive :person-role-edit-project-form ::pipelines/controller)

(def pipelines
  {:keechma.form/submit-data (pipeline! [value {:keys [deps-state*] :as ctrl}]
                                        (m! [:update-person-role-by-project [:updatePersonRoleByProject]] {:personroleid (get-in @deps-state* [:router :person-role])
                                                                                                           :person  (:person value)
                                                                                                           :role    (:role value)
                                                                                                           :date    (:date value)}))
   :delete                   (pipeline! [value {:keys [deps-state*] :as ctrl}]
                                        (m! [:delete-person-role [:deletePersonRole]] {:personrole (str (get-in @deps-state* [:router :person-role]))}))})

(defmethod ctrl/start :person-role-edit-project-form [_ state _ _]
           {:is-form-open? nil})

(defmethod ctrl/prep :person-role-edit-project-form [ctrl]
           (pipelines/register ctrl
                               (form/wrap pipelines
                                          (v/to-validator {}))))
