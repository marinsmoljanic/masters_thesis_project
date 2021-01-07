(ns app.controllers.forms.role-edit
  (:require [keechma.next.controller :as ctrl]
            [keechma.next.controllers.pipelines :as pipelines]
            [keechma.pipelines.core :as pp :refer-macros [pipeline!]]
            [keechma.next.controllers.form :as form]
            [keechma.next.controllers.router :as router]
            [keechma.next.controllers.entitydb :as edb]
            [app.gql :refer [m!]]
            [app.validators :as v]))

(derive :role-edit-form ::pipelines/controller)

(def pipelines
  {:keechma.form/get-data    (pipeline! [value {:keys [deps-state*] :as ctrl}]
                                        ;; (println "FORM " (:id value)  " - " value)
                                        value)

   :keechma.form/submit-data (pipeline! [value {:keys [deps-state*] :as ctrl}]
                                        (println "VALUE from controler: " value)
                                        (m! [:update-role [:updateRole]] {:id (:id value)
                                                                          :name (:Name value)})
                                        #_(ctrl/broadcast ctrl :anon/login value)
                                        (router/redirect! ctrl :router {:page ""}))
   :delete                   (pipeline! [value {:keys [deps-state*] :as ctrl}]
                                        ;;(println "Value: " value)
                                        (m! [:delete-role [:deleteRole]] {:id value})
                                        #_(ctrl/broadcast ctrl :anon/login value)
                                        (router/redirect! ctrl :router {:page ""}))
   })

(defmethod ctrl/prep :role-edit-form [ctrl]
           (pipelines/register ctrl
                               (form/wrap pipelines
                                          (v/to-validator {}))))
