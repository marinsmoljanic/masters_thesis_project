(ns app.ui.pages.home
  "Example homepage 2 3"
  (:require [helix.dom :as d]
            [helix.core :as hx :refer [$]]
            [keechma.next.helix.core :refer [with-keechma]]
            [keechma.next.helix.lib :refer [defnc]]
            [keechma.next.helix.classified :refer [defclassified]]
            [app.ui.components.main :refer [Main]]
            [app.ui.components.hello :refer [Hello]]))

(defclassified HomepageWrapper :div "flex flex-col h-screen w-screen bg-gray-800")

(defnc HomeRenderer [_]
       ($ HomepageWrapper
          (d/div {:class "flex w-fullnitems-center shadow justify-center py-6 px-2 bg-gray-900 text-gray-700 text-2xl font-thin"}
                 (d/p "Project Management App"))
          (d/div {:class "flex h-screen w-full flex-col items-center justify-center px-2"}
                 (d/button {:class "flex rounded w-2/3 justify-center my-8 py-2 px-8 bg-orange-600 text-white text-xl font-thin"}
                           "Osoba")
                 (d/button {:class "flex rounded w-2/3 justify-center my-8 py-2 px-8 bg-orange-600 text-white text-xl font-thin"}
                           "Projekt")
                 (d/button {:class "flex rounded w-2/3 justify-center my-8 py-2 px-8 bg-orange-600 text-white text-xl font-thin"}
                           "Uloga")
                 (d/button {:class "flex rounded w-2/3 justify-center my-8 py-2 px-8 bg-orange-600 text-white text-xl font-thin"}
                           "Uloga osobe")
                 )))

(def Home (with-keechma HomeRenderer))