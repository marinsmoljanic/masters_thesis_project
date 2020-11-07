(ns app.controllers.jwt
  (:require [keechma.next.controller :as ctrl]
            [keechma.next.controllers.pipelines :as pipelines]
            [hodgepodge.core :refer [local-storage get-item set-item remove-item]]
            [app.domain.settings :refer [jwt-ls-name]]
            [keechma.next.controllers.router :as router]
            [keechma.pipelines.core :as pp :refer-macros [pipeline!]]
            [promesa.core :as p]
            [keechma.next.toolbox.logging :as l]
            [app.gql :refer [q!]]))

(derive :jwt ::pipelines/controller)

(defn set-jwt! [state* jwt] (set-item local-storage jwt-ls-name jwt) (reset! state* jwt))

(defn clear-jwt! [state*] (remove-item local-storage jwt-ls-name) (reset! state* nil))

(def pipelines
  {:anon/login   (pipeline! [value {:keys [state*]}]
                            (set-jwt! state* (get-in value [:session :token])))
   :anon/log-out (pipeline! [value {:keys [state*]}]
                            (clear-jwt! state*))})

(defmethod ctrl/prep :jwt [ctrl]
  (pipelines/register ctrl pipelines))

(defmethod ctrl/derive-state :jwt [_ state]
  (get-item local-storage jwt-ls-name))