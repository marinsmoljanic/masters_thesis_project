(ns app.ui.pages.uloga
  (:require [helix.dom :as d]
            [helix.core :as hx :refer [$]]
            [keechma.next.helix.lib :refer [defnc]]
            [keechma.next.controllers.router :as router]
            [keechma.next.helix.core :refer [with-keechma dispatch]]
            [keechma.next.helix.classified :refer [defclassified]]

            [app.ui.components.header :refer [Header]]))

(defclassified PageWrapper :div "flex flex-col h-screen w-screen bg-gray-800")

(def uloge
  [{:id    1
    :naziv "Uloga 1"}
   {:id    2
    :naziv "Uloga 2"}
   {:id    3
    :naziv "Uloga 3"}
   {:id    4
    :naziv "Uloga 4"}
   {:id    5
    :naziv "Uloga 5"}])

(defnc TableItem [props]
       (d/tr {:class "border-b border-solid border-gray-700"}
             (d/td {:class "pl-2 py-2 text-white"} (:naziv props))))

(defnc Table [props]
       (d/div {:class "flex h-screen w-full flex-col items-center"}
              (d/table {:class "table-fixed w-full top-0"}
                       (d/thead (d/tr {:class "border-b border-t border-solid border-orange-500 bg-gray-700 text-gray-900"}
                                      (d/th {:class "w-1/4 px-4 py-2 font-base"} "Naziv uloge")))
                       (d/tbody
                         (map #($ TableItem {:naziv (:naziv %)
                                             :key   (:id %)})
                              uloge)))))

(defnc Renderer [props]
       ($ PageWrapper
          ($ Header {:naslov "Uloga"})
          ($ Table)

          (d/div {:class "flex justify-end py-8 px-8 absolute bottom-0 w-full"}
                 (d/button {:class "rounded-full bg-orange-600 text-white h-20 w-20 justify-center items-center text-4xl font-thin"} "+"
                           :on-click #(dispatch props :person-form :toggle nil)))))

(def Uloga (with-keechma Renderer))