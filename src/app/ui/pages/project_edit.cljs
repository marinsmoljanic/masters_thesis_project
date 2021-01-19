(ns app.ui.pages.project-edit
  (:require [helix.dom :as d]
            [helix.core :as hx :refer [$]]
            [keechma.next.helix.lib :refer [defnc]]
            [keechma.next.controllers.router :as router :refer [redirect!]]
            [keechma.next.helix.core :refer [with-keechma dispatch]]
            [keechma.next.helix.classified :refer [defclassified]]
            [keechma.next.helix.core :refer [with-keechma use-meta-sub dispatch call use-sub]]
            [keechma.next.toolbox.logging :as l]
            [tick.alpha.api :as t]
            [app.ui.components.inputs :refer [wrapped-input]]
            [app.ui.components.header :refer [Header]]))

(defclassified PageWrapper :div "flex flex-col h-screen w-screen bg-gray-800
                                 md:h-full md:mx-auto md:w-2/3 shadow")

(defnc TableItem [{:keys [personId personName roleId assignmentDate roleName project-code project-name person-role-id] :as props}]
       (d/tr {:class "border-b border-solid border-gray-700  hover:bg-gray-900 cursor-pointer"
              :on-click #(redirect! props :router {:page            "ulogaosobeurediprojektnobazirano"
                                                   :person-role     person-role-id
                                                   :project-name    project-name
                                                   :project-id      project-code
                                                   :person-name     personName
                                                   :person-id       personId
                                                   :assignment-date assignmentDate
                                                   :role-name       roleName
                                                   :role-id         roleId})}

             (d/td {:class "pl-2 py-2 text-white"} personName)
             (d/td {:class "pl-2 py-2 text-white"} roleName)
             (d/td {:class "pl-2 py-2 text-white"} assignmentDate)))

(defnc RenderErrors [{:keys [error] :as props}]
       (d/div {:class "text-redDark text-xs pt-2"}
              error))

(defnc Renderer [props]
       (let [route (use-sub props :router)
             project-name (:name route)
             person-roles (use-sub props :person-role-by-projectid)
             roles (use-sub props :roles)
             persons (use-sub props :persons)]
            ($ PageWrapper
               ($ Header {:naslov "Uredi podatke projekta"})
               (d/div {:class "min-w-full mt-8 px-4 text-white
                               md:min-h-full md:pb-4"}
                      (d/form {:on-submit (fn [e]
                                              (.preventDefault e)
                                              (dispatch props :project-edit-form :keechma.form/submit))}

                              (d/p {:class "text-sm text-grayLight text-left w-full mb-6"} "Naziv projekta")
                              (wrapped-input {:keechma.form/controller :project-edit-form
                                              :input/type              :text
                                              :input/attr              :name
                                              :placeholder             "Naziv projekta"})

                              (d/div {:class "max-width-full h-6 transparent"})
                              (d/p {:class "text-sm text-grayLight text-left w-full mb-6"} "Opis projekta")
                              (wrapped-input {:keechma.form/controller :project-edit-form
                                              :input/type              :text
                                              :input/attr              :description
                                              :placeholder             "Opis projekta"})
                              (d/p {:class "text-sm text-grayLight text-left w-full mb-6"} "Datum pocetka")
                              (wrapped-input {:keechma.form/controller :project-edit-form
                                              :input/type              :date
                                              :input/attr              :startDate
                                              :placeholder             "Datum pocetka"})

                              (d/div {:class "max-width-full h-6 transparent"})
                              (d/p {:class "text-sm text-grayLight text-left w-full mb-6"} "Datum zavrsetka")
                              (wrapped-input {:keechma.form/controller :project-edit-form
                                              :input/type              :date
                                              :input/attr              :endDate
                                              :placeholder             "Datum zavrsetka"})

                              (d/div {:class "flex flex-row justify-between px-10 pt-4"}
                                     (d/button {:class "block margin-auto border w-56 px-4 py-3 rounded-sm
                                                        text-md font-medium text-white bg-transparent hover:bg-gray-700 hover:border-red-600"
                                                :type  "button"
                                                :on-click #(dispatch props :project-edit-form :delete-project nil)}
                                               "Obrisi")

                                     (d/button {:class "block margin-auto border w-56 px-4 py-3 rounded-sm
                                                        text-md font-medium text-white bg-transparent hover:bg-gray-700"}
                                               "Spremi")))

                      (d/div {:class "w-full mt-12  flex flex-row border-t border-dotted border-gray-600"}
                             (d/div {:class "flex justify-start text-normal font-thin text-gray-400 pt-4 pb-2 w-1/2 "}
                                    "Uloge djelatnika")

                             (d/div {:class "flex justify-end text-normal text-gray-400 pt-4 pb-2 w-1/2"}
                                    (d/button {:class "border-dotted font-thin border border-gray-400 px-2 py-1 hover:bg-gray-700 hover:text-white
                                                                      focus:outline-none active:bg-gray-900"
                                               :on-click #(redirect! props :router {:page "ulogaosobe"})}
                                              "+ Dodaj novo zaduženje")))

                      (d/table {:class "table-fixed w-full"}
                               (d/thead (d/tr {:class "border-b border-t border-solid border-orange-500 bg-gray-700 text-gray-900"
                                               }
                                              (d/th {:class "w-1/2 px-4 py-2 font-base border-l border-solid border-orange-500"} "Osoba")
                                              (d/th {:class "w-1/2 px-4 py-2 font-base border-l border-r border-solid border-orange-500"} "Uloga")
                                              (d/th {:class "w-1/2 px-4 py-2 font-base border-l border-r border-solid border-orange-500"} "Datum zaduzenja")))
                               (d/tbody
                                 (map (fn [person-role]
                                          ($ TableItem {:personId       (:PersonId person-role)
                                                        :personName     (:personName person-role)
                                                        :roleId         (:RoleId person-role)
                                                        :assignmentDate (:AssignmentDate person-role)
                                                        :roleName       (:roleName person-role)
                                                        :project-code   (:ProjectCode person-role)
                                                        :project-name   project-name
                                                        :key            (:id person-role)
                                                        :person-role-id (:id person-role)
                                                        &        props}))
                                      person-roles)))))))


(def ProjectEdit (with-keechma Renderer))