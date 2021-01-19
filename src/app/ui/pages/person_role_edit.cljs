(ns app.ui.pages.person-role-edit
  (:require [helix.dom :as d]
            [helix.core :as hx :refer [$]]
            [keechma.next.toolbox.logging :as l]
            [keechma.next.helix.lib :refer [defnc]]
            [app.ui.components.header :refer [Header]]
            [keechma.next.controllers.router :as router]
            [app.ui.components.inputs :refer [wrapped-input]]
            [keechma.next.helix.core :refer [with-keechma dispatch]]
            [keechma.next.helix.classified :refer [defclassified]]
            [keechma.next.helix.core :refer [with-keechma use-meta-sub dispatch call use-sub]]))

(defclassified PageWrapper :div "flex flex-col h-screen w-screen bg-gray-800
                                 md:h-full md:mx-auto md:w-2/3 md:pb-2 md:pb-8 shadow")

(defnc RenderErrors [{:keys [error] :as props}]
       (d/div {:class "text-redDark text-xs pt-2"}
              error))

(defnc Renderer [props]
       (let [persons (get-in (use-sub props :persons) [:allPerson])
             persons-map {}
             persons-select-value (map (fn [person]
                                           (assoc persons-map :label (str (:FirstName person) " " (:LastName person)) :value (:id person)))
                                       persons)
             roles (use-sub props :roles)
             roles-map {}
             roles-select-value (map (fn [role]
                                         (assoc roles-map :label (:Name role) :value (:id role)))
                                     roles)
             projects (get-in (use-sub props :projects) [:allProject])
             projects-map {}
             projects-select-value (map (fn [project]
                                            (assoc projects-map :label (:Name project) :value (:id project)))
                                        projects)

             route (use-sub props :router)
             person-name (:person-name route)
             person-surname (:person-surname route)
             person-id (:id route)
             project (:project route)]
            ($ PageWrapper
               ($ Header {:naslov "Uređivanje zaduženja"})

               (d/div {:class "m-auto min-w-full mt-8 px-4 text-white"}
                      (d/form {:on-submit (fn [e]
                                              (.preventDefault e)
                                              (dispatch props :person-role-edit-form :keechma.form/submit)
                                              (router/back! props :router))}

                              (d/div {:class "text-3xl text-white text-center w-full mb-6 mt-6"}
                                   (str person-name " " person-surname))

                              (d/p {:class "text-sm text-grayLight text-left w-full mb-6"} "Projekt")
                              (wrapped-input {:keechma.form/controller :person-role-edit-form
                                              :input/type :select
                                              :input/attr :project
                                              :options    projects-select-value})

                              (d/p {:class "text-sm text-grayLight text-left w-full mb-6 pt-6"} "Uloga")
                              (wrapped-input {:keechma.form/controller :person-role-edit-form
                                              :input/type :select
                                              :input/attr :role
                                              :options    roles-select-value})

                              (d/p {:class "text-sm text-grayLight text-left w-full mb-6 pt-6"} "Datum dodjele")
                              (wrapped-input {:keechma.form/controller :person-role-edit-form
                                              :input/type              :date
                                              :input/attr              :date
                                              :placeholder             "DD/MM/GG"})

                              (d/div {:class "w-full mt-8 flex flex-row justify-between px-12"}
                                     (d/button {:class    "block margin-auto mx-auto border w-56 px-4 py-3 rounded-sm
                                                       text-md font-medium text-white bg-transparent hover:bg-gray-700 hover:border-red-400"
                                                :type     "button"
                                                :on-click (fn [_] (dispatch props :person-role-edit-form :delete)
                                                                  (router/back! props :router))} "Obrisi zaduzenje")
                                     (d/button {:class    "block margin-auto mx-auto border w-56 px-4 py-3 rounded-sm
                                                       text-md font-medium text-white bg-transparent hover:bg-gray-700"} "Pohrani promjene"))

                              )))))

(def PersonRoleEditForm (with-keechma Renderer))