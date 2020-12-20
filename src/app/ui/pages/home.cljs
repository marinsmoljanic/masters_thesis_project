(ns app.ui.pages.home
  (:require [helix.dom :as d]
            [helix.core :as hx :refer [$]]
            [keechma.next.helix.lib :refer [defnc]]
            [keechma.next.controllers.router :as router]
            [keechma.next.helix.core :refer [with-keechma]]
            [keechma.next.helix.classified :refer [defclassified]]))

(defclassified HomepageWrapper :div "flex flex-col h-screen w-screen bg-gray-800")

(defnc HomeRenderer [props]
       ($ HomepageWrapper
          (d/div {:class "flex w-full items-center shadow justify-center py-6 px-2 bg-gray-900 text-gray-700 text-2xl font-thin"}
                 (d/p "Aplikacija upravljanja projektima"))
          (d/div {:class "flex h-screen w-full flex-col items-center justify-center px-2"}
                 (d/button {:class "flex rounded w-2/3 justify-center my-8 py-2 px-8 border border-solid border-orange-400 hover:bg-gray-700 text-white text-xl font-thin"
                            :on-click #(router/redirect! props :router {:page "osoba"})}
                           "Osobe")
                 (d/button {:class "flex rounded w-2/3 justify-center my-8 py-2 px-8 border border-solid border-orange-400 hover:bg-gray-700 text-white text-xl font-thin"
                            :on-click #(router/redirect! props :router {:page "projekt"})}
                           "Projekti")
                 (d/button {:class "flex rounded w-2/3 justify-center my-8 py-2 px-8 border border-solid border-orange-400 hover:bg-gray-700 text-white text-xl font-thin"
                            :on-click #(router/redirect! props :router {:page "uloga"})}
                           "Uloge"))))

(def Home (with-keechma HomeRenderer))