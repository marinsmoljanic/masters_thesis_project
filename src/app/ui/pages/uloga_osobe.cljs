(ns app.ui.pages.uloga-osobe
  "Example homepage 2 3"
  (:require [helix.dom :as d]
            [helix.core :as hx :refer [$]]
            [keechma.next.helix.core :refer [with-keechma]]
            [keechma.next.controllers.router :as router]

            [keechma.next.helix.lib :refer [defnc]]
            [keechma.next.helix.classified :refer [defclassified]]
            [app.ui.components.main :refer [Main]]
            [app.ui.components.hello :refer [Hello]]))

(defclassified PageWrapper :div "flex flex-col h-screen w-screen bg-gray-800")

(defnc Renderer [props]
       ($ PageWrapper
          (d/div {:class "flex flex-row w-full items-center shadow justify-center py-6 px-2 bg-gray-900 text-gray-700 text-2xl font-thin"}
                 (d/div {:class "flex text-base w-1/6 items-center justify-center cursor-pointer"
                         :on-click #(router/back! props :router)} "< Back")
                 (d/div {:class "flex w-5/6 text-gray-500 items-center justify-center"} "Uloge osoba")
                 (d/div {:class "flex w-1/6"} ""))
          (d/div {:class "flex h-screen w-full flex-col items-center justify-center px-2"}
                 "Podatci o ulogama osoba"
                 )))

(def UlogaOsobe (with-keechma Renderer))