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

(defclassified PageWrapper :div "flex flex-col h-screen w-screen bg-gray-800 relative")

(defnc TableItem [props]
       (d/tr {:class "border-b border-solid border-gray-700 cursor-pointer hover:bg-gray-900"
              :on-click #(redirect! props :router {:page "projectedit"
                                                   :id (:id props)})}
             (d/td {:class "pl-2 py-2 text-white"} (:name props))
             (d/td {:class "pl-2 py-2 text-white"} (:description props))))

(defnc Renderer [props]
       (let [is-project-form-open? (get-in (use-sub props :project-form) [:is-project-form-open?])
             projects              (get-in (use-sub props :projects) [:allProject])]
         ($ PageWrapper
            (if is-project-form-open?
              ($ HeaderCreateForm {:naslov "Popis projekata"})
              ($ Header {:naslov "Popis projekata"}))

            (if is-project-form-open?
              ($ ProjectForm)

            (d/div {:class "flex h-screen w-full flex-col items-center"}
                   (d/table {:class "table-fixed w-full top-0"}
                            (d/thead (d/tr {:class "border-b border-t border-solid border-orange-500 bg-gray-700 text-gray-900"}
                                           (d/th {:class "w-1/3 px-4 py-2 font-base"} "Naziv projekta")
                                           (d/th {:class "w-1/3 px-4 py-2 font-base border-l border-solid border-orange-500"} "Opis projekta")))
                            (d/tbody
                              (map #($ TableItem {:name        (:Name %)
                                                  :description (:Description %)
                                                  :id          (:id %)
                                                  :key         (:id %)
                                                  &            props})
                                   projects)))))

            (d/div {:class "flex justify-end py-8 px-8 absolute bottom-0 w-full"}
                   (d/button {:class (str (if is-project-form-open? "bg-transparent border-solid border-2 border-red-600 " "bg-orange-600 ") "rounded-full text-white h-20 w-20 justify-center items-center text-4xl font-thin focus:outline-none")
                              :on-click #(dispatch props :project-form :toggle nil)}
                             (if is-project-form-open? "x" "+"))))))

(def Project (with-keechma Renderer))