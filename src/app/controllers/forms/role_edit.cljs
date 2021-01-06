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
  {
   ;; :load-data
   ;; (pipeline! [_ {:keys [deps-state*]}]
   ;;           (let [roles (edb/get-named (:entitydb @deps-state*) :roles-data)]
   ;;                {:name (edb/get-named (:entitydb @deps-state*) :roles-data)}))
   ;; :delete-entity (pipeline! [_ {:keys [deps-state*]}]
   ;;                         (let [roles (edb/get-named (:entitydb @deps-state*) :roles-data)]
   ;;                               {:name (edb/get-named (:entitydb @deps-state*) :roles-data)}))

   ;; :click (pipeline! [_ {:keys [deps-state*]}]
   ;;                (println "CLICK PIPLANE KEY"))

   :keechma.form/get-data (pipeline! [value {:keys [deps-state*] :as ctrl}]
                                     #_(println "FORM " (:id value) " - " value)
                                     value)

   :keechma.form/submit-data (pipeline! [value {:keys [deps-state*]}]
                                        (println "VALUE from controler: " value)
                                        #_(m! [:login [:login :token]] {:input value})
                                        #_(ctrl/broadcast ctrl :anon/login value)
                                        #_(router/redirect! ctrl :router {:page "osoba"}))})

(defmethod ctrl/prep :role-edit-form [ctrl]
           (pipelines/register ctrl
                               (form/wrap pipelines
                                          (v/to-validator {}))))
