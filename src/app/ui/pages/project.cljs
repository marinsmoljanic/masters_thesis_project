(ns app.ui.pages.project
  (:require [helix.dom :as d]
            [helix.core :as hx :refer [$]]
            [keechma.next.helix.lib :refer [defnc]]
            [keechma.next.controllers.router :as router]
            [keechma.next.helix.core :refer [with-keechma dispatch call use-sub]]
            [keechma.next.helix.classified :refer [defclassified]]
            [keechma.next.controllers.router :refer [redirect!]]

            [app.ui.components.header :refer [Header HeaderCreateForm]]
            [app.ui.components.forms.projekt :refer [ProjectForm]]))

(defclassified PageWrapper :div "flex flex-col h-screen w-screen bg-gray-800
                                 md:h-full md:mx-auto md:w-2/3 shadow")

(defnc TableItem [props]
       (d/tr {:class "border-b border-solid border-gray-700 cursor-pointer hover:bg-gray-900"
              :on-click #(redirect! props :router {:page "projectedit"
                                                   :id (:id props)
                                                   :description (:description props)
                                                   :name (:name props)
                                                   :startDate (:startDate props)
                                                   :endDate (:endDate props)})}
             (d/td {:class "pl-2 py-2 text-white"} (:name props))
             (d/td {:class "pl-2 py-2 text-white"} (:description props))
             (d/td {:class "pl-2 py-2 text-white"} (:startDate props))
             (d/td {:class "pl-2 py-2 text-white"} (:endDate props))))

(defnc Renderer [props]
       (let [is-project-form-open? (get-in (use-sub props :project-form) [:is-project-form-open?])
             projects              (get-in (use-sub props :projects) [:allProject])]
         ($ PageWrapper
            (if is-project-form-open?
              ($ HeaderCreateForm {:naslov "Unesite podatke novog projekata"})
              ($ Header {:naslov "Popis projekata"}))

            (if is-project-form-open?
              (d/div {:class "md:pb-8 md:border-solid md:border-b-2 md:border-l md:border-r md:border-orange-400"}
                ($ ProjectForm)
                (d/div {:class "flex justify-end py-8 px-8 absolute w-full
                                md:w-2/3 md:mt-8"}
                       (d/button {:class "bg-transparent border-solid border-2 border-red-600 rounded-full text-white h-20 w-20 justify-center items-center text-4xl font-thin focus:outline-none
                                          md:bg-gray-600 md:justify-end md:border-red-800"
                                  :on-click #(dispatch props :project-form :toggle nil)}
                                 "x")))


            (d/div {:class "flex w-full flex-col items-center
                            md:border-solid md:border-b-2 md:border-l md:border-r md:border-orange-400"}
                   (d/table {:class "table-fixed w-full top-0"}
                            (d/thead (d/tr {:class "border-b border-t border-solid border-orange-500 bg-gray-700 text-gray-900"}
                                           (d/th {:class "w-1/3 px-4 py-2 font-base"} "Naziv projekta")
                                           (d/th {:class "w-1/3 px-4 py-2 font-base border-l border-solid border-orange-500"} "Opis projekta")
                                           (d/th {:class "w-1/3 px-4 py-2 font-base border-l border-solid border-orange-500"} "Datum pocetka")
                                           (d/th {:class "w-1/3 px-4 py-2 font-base border-l border-solid border-orange-500"} "Datum zavrsetka")))
                            (d/tbody
                              (map #($ TableItem {:name        (:Name %)
                                                  :description (:Description %)
                                                  :startDate   (:StartDate %)
                                                  :endDate     (:EndDate %)
                                                  :id          (:id %)
                                                  :key         (:id %)
                                                  &            props})
                                   projects))
                            (d/div {:class "flex justify-end py-8 px-8 absolute w-full
                                            md:w-2/3"}
                                   (d/button {:class (str (if is-project-form-open? "bg-transparent border-solid border-2 border-red-600 " "bg-orange-600 ") "rounded-full text-white h-20 w-20 justify-center items-center text-4xl font-thin focus:outline-none
                                                                                     md:bg-orange-600")
                                              :on-click #(dispatch props :project-form :toggle nil)}
                                             (if is-project-form-open? "x" "+")))))))))

(def Project (with-keechma Renderer))