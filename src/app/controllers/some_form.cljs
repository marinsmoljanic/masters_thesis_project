(ns app.controllers.some-form
  (:require [keechma.next.controller :as ctrl]
            [keechma.next.controllers.pipelines :as pipelines]
            [keechma.pipelines.core :refer-macros [pipeline!]]
            [keechma.next.controllers.form :as form]
            [keechma.next.controllers.router :as router]
            [app.validators :as v]))

(derive :some-form ::pipelines/controller)

(def pipelines
  {:keechma.form/get-data    (pipeline! [value {:keys [deps-state*] :as ctrl}]
                               (println "FORM " (:id value)  " - " value)
                               value)

   :keechma.form/submit-data (pipeline! [value {:keys [deps-state*] :as ctrl}]
                               (println "SUBMIT VALUE" value)
                               (ctrl/dispatch ctrl :some-data :update-name value)
                               )})

(defmethod ctrl/prep :some-form [ctrl]
  (pipelines/register ctrl
    (form/wrap pipelines
      (v/to-validator {}))))