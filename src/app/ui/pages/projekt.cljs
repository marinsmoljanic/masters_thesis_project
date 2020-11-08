(ns app.ui.pages.projekt
  (:require [helix.dom :as d]
            [helix.core :as hx :refer [$]]
            [keechma.next.helix.lib :refer [defnc]]
            [keechma.next.controllers.router :as router]
            [keechma.next.helix.core :refer [with-keechma]]
            [keechma.next.helix.classified :refer [defclassified]]

            [app.ui.components.header :refer [Header]]
            [app.ui.components.pure.shared :refer [AddNewItem]]))

(defclassified PageWrapper :div "flex flex-col h-screen w-screen bg-gray-800 relative")

(def projekti
  [{:id    1
    :naziv "Projekt 1"}
   {:id    2
    :naziv "Projekt 2"}
   {:id    3
    :naziv "Projekt 3"}
   {:id    4
    :naziv "Projekt 4"}
   {:id    5
    :naziv "Projekt 5"}])

(defnc TableItem [props]
       (d/tr {:class "border-b border-solid border-gray-700"}
             (d/td {:class "pl-2 py-2 text-white"} (:naziv props))))

(defnc Table [props]
       (d/div {:class "flex h-screen w-full flex-col items-center"}
              (d/table {:class "table-fixed w-full top-0"}
                       (d/thead (d/tr {:class "border-b border-t border-solid border-orange-500 bg-gray-700 text-gray-900"}
                                      (d/th {:class "w-1/4 px-4 py-2 font-base"} "Naziv projekta")))
                       (d/tbody
                         (map #($ TableItem {:naziv (:naziv %)
                                             :key   (:id %)})
                              projekti)))))

(defnc Renderer [props]
       ($ PageWrapper
          ($ Header {:naslov "Projekt"})
          ($ Table)
          ($ AddNewItem)))

(def Projekt (with-keechma Renderer))