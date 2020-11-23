(ns app.controllers.datasources.person
  (:require [app.gql :refer [q!]]
            [keechma.pipelines.core :as pp :refer-macros [pipeline!]]

            [keechma.next.controller :as ctrl]
            [keechma.next.controllers.entitydb :as edb]
            [keechma.next.controllers.pipelines :as pipelines]))

(derive :person ::pipelines/controller)

(def load-persons
  (-> (pipeline! [value {:keys [deps-state*] :as ctrl}]
                 (println "Prije poziva -------------------->")
                 (q! [:osobe [:allOsoba]] {})
                 #! (edb/insert-named! ctrl :entitydb :courses :courses-data value)
                 (println "-------------------->" value))
      (pp/set-queue :load-persons)))

(def pipelines
  {:keechma.on/start load-persons})

(defmethod ctrl/prep :person [ctrl]
  (pipelines/register ctrl pipelines))

;; (defmethod ctrl/derive-state :person [_ state {:keys [entitydb]}]
;;  (edb/get-named entitydb :courses-data))


