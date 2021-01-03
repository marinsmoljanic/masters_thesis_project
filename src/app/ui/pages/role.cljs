(ns app.ui.pages.role
  (:require [helix.dom :as d]
            [helix.core :as hx :refer [$]]
            [keechma.next.helix.lib :refer [defnc]]
            [keechma.next.controllers.router :as router]
            [keechma.next.helix.core :refer [with-keechma dispatch use-sub]]
            [keechma.next.helix.classified :refer [defclassified]]

            [app.ui.components.inputs :refer [wrapped-input]]
            [app.ui.components.header :refer [Header]]))

(defclassified PageWrapper :div "flex flex-col h-screen w-screen bg-gray-800")

(defnc RoleListItem [props]
       (d/div {:class "w-full pt-4 px-4 text-white border-b border-gray-700 border-solid"}
              (d/form {:on-submit (fn [e]
                                     (.preventDefault e)
                                      (println "-------------------------------")
                                      #_(dispatch props :role-edit-form :click)
                                      (dispatch props :role-edit-form :keechma.form/submit))}

                     (d/div {:class "flex flex-row w-full"}
                         (wrapped-input {:keechma.form/controller :role-edit-form
                                         :input/type              :textPlain
                                         :input/attr              :name
                                         :value                   (:naziv props)
                                         :placeholder             "Naziv Uloge"})

                         (d/div {:class "flex flex-row justify-between px-10"}
                                (d/button {:class "block margin-auto mb-4 px-2 rounded-sm border-gray-600
                                                                          text-md font-medium text-white bg-transparent hover:bg-gray-900
                                                                          border hover:border-red-600 focus:outline-none"
                                           :type "button"
                                           :on-click #(println "KLIK NA BRISANJE")} "Obrisi")

                                (d/button {:class "block margin-auto mb-4 px-2 rounded-sm border-gray-600 ml-2
                                                                          text-md font-medium text-white bg-transparent hover:bg-gray-900
                                                                          border hover:border-green-600 focus:outline-none"} "Spremi")

                          )))))

(defnc Renderer [props]
       (let [roles (get-in (use-sub props :roles) [:allRole])]
         ($ PageWrapper
          ($ Header {:naslov "Šifrarnik uloga"})

          (d/div {:class "flex w-full flex-col items-center"}
                (map (fn [role] ($ RoleListItem {:naziv (:Name role)
                                                 :id    (:id role)
                                                 :key   (:id role)}))
                     roles))

          (d/div {:class "w-full pt-4 px-4 text-white"}
                 (d/form {:on-submit (fn [e]
                                         (.preventDefault e)
                                         (println "Prije dispatcha na kontroler")
                                         #_(dispatch props :role-edit-form :click nil)
                                         (dispatch props :role-form :keechma.form/submit))}

                         (d/div {:class "flex flex-row w-full"}
                                (wrapped-input {:keechma.form/controller :role-form
                                                :input/type              :textPlain
                                                :input/attr              :name
                                                :placeholder             "Naziv nove uloge"})

                                (d/div {:class "flex flex-row justify-between px-10"}
                                       (d/button {:class "block margin-auto mb-4 px-2 rounded-sm border-gray-600
                                                                        text-md font-medium text-white bg-transparent hover:bg-gray-900
                                                                        border hover:border-green-600 focus:outline-none"} "Spremi"))


                                )))



            )))

(def Role (with-keechma Renderer))