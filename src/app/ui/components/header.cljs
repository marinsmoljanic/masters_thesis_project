(ns app.ui.components.header
  (:require [helix.dom :as d]
            [helix.hooks :as hooks]
            [helix.core :as hx :refer [<>]]
            [keechma.next.helix.lib :refer [defnc]]
            [keechma.next.controllers.router :as router]
            [keechma.next.helix.core :refer [with-keechma]]))



(defnc Renderer [props]
       (d/div {:class "flex flex-row w-full items-center shadow justify-center py-6 px-2 bg-gray-900 text-gray-700 text-2xl"}
              (d/div {:class    "flex text-base w-1/6 items-center justify-center cursor-pointer"
                      :on-click #(router/back! props :router)} "< Back")
              (d/div {:class "flex w-5/6 text-gray-500 items-center justify-center font-normal"} (:naslov props))
              (d/div {:class "flex w-1/6"} "")))

(def Header (with-keechma Renderer))
