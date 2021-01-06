(ns app.ui.pages.person-edit
  (:require [helix.dom :as d]
            [helix.core :as hx :refer [$]]
            [keechma.next.helix.lib :refer [defnc]]
            [keechma.next.controllers.router :as router :refer [redirect!]]
            [keechma.next.helix.core :refer [with-keechma dispatch]]
            [keechma.next.helix.classified :refer [defclassified]]
            [keechma.next.helix.core :refer [with-keechma use-meta-sub dispatch call use-sub]]

            [app.ui.components.inputs :refer [wrapped-input]]
            [keechma.next.toolbox.logging :as l]

            [app.ui.components.header :refer [Header]]))

(defclassified PageWrapper :div "flex flex-col h-screen w-screen bg-gray-800")

(defnc TableItem [props]
       (d/tr {:class "border-b border-solid border-gray-700"}
             (d/td {:class "pl-2 py-2 text-white hover:bg-gray-900 cursor-pointer"
                    :on-click #(redirect! props :router {:page "projectedit"
                                                         :id (:projectId props)})} (:projectName props))
             (d/td {:class "pl-2 py-2 text-white hover:bg-gray-900 cursor-pointer"
                    :on-click #(redirect! props :router {:page "roleedit"
                                                         :id (:roleId props)})} (:roleName props))
             (d/td {:class "pl-2 py-2 text-white"} (:assignmentDate props))))

(defnc RenderErrors [{:keys [error] :as props}]
       (d/div {:class "text-redDark text-xs pt-2"}
              error))

(defn extract-name [person-role-id roles]
      (l/pp (get-in (first (filterv (fn [role]
                                        (= (:id role) person-role-id))
                                    roles)) [:Name])))

(defnc Renderer [props]
       (let [
             ;; person-roles (get-in (use-sub props :person-role-by-personid) [:personRoleByPersonid])
             roles (get-in (use-sub props :roles) [:allRole])
             projects (get-in (use-sub props :projects) [:allProject])
             ;; extracted-role-name (extract-name (:RoleId person-roles) roles)

             ;;_ (l/pp person-roles)
           ]
               ($ PageWrapper
                      ($ Header {:naslov "Uredi podatke osobe"})
                             (d/div {:class "min-w-full min-h-screen mt-8 px-4 text-white"}
                                    (d/form {:on-submit (fn [e]
                                                          (.preventDefault e)
                                                          (dispatch props :person-edit-form :keechma.form/submit))}

                                            (d/p {:class "text-sm text-grayLight text-left w-full mb-6"} "Ime osobe")
                                            (wrapped-input {:keechma.form/controller :person-edit-form
                                                            :input/type              :text
                                                            :input/attr              :firstName
                                                            :placeholder             "Ime"})

                                            (d/div {:class "max-width-full h-6 transparent"})
                                            (d/p {:class "text-sm text-grayLight text-left w-full mb-6"} "Prezime osobe")
                                            (wrapped-input {:keechma.form/controller :person-edit-form
                                                            :input/type              :text
                                                            :input/attr              :lastName
                                                            :placeholder             "Prezime"})

                                            (d/div {:class "flex flex-row justify-between px-10 pt-4"}
                                                   (d/button {:class "block margin-auto  border w-56 px-4 py-3 rounded-sm
                                                                      text-md font-medium text-white bg-transparent hover:bg-gray-700 hover:border-red-600
                                                                      focus:outline-none active:bg-gray-900"
                                                              :type  "button"
                                                              :on-click #(dispatch props :person-edit-form :delete-person nil)} "Obrisi")

                                                   (d/button {:class "block margin-auto border w-56 px-4 py-3 rounded-sm
                                                                      text-md font-medium text-white bg-transparent hover:bg-gray-700
                                                                      active:bg-gray-900 focus:outline-none"} "Spremi")))

                                    (d/div {:class "w-full mt-12  flex flex-row border-t border-dotted border-gray-600"}
                                           (d/div {:class "flex justify-start text-normal font-thin text-gray-400 pt-4 pb-2 w-1/2 "}
                                                  "Zaduzenja osobe")

                                           (d/div {:class "flex justify-end text-normal text-gray-400 pt-4 pb-2 w-1/2"}
                                                  (d/button {:class "border-dotted font-thin border border-gray-400 px-2 py-1 hover:bg-gray-700 hover:text-white
                                                                      focus:outline-none active:bg-gray-900"
                                                             :on-click #(redirect! props :router {:page "ulogaosobe"})}
                                                        "+ Dodaj novo zaduzenje")))

                                    (d/table {:class "table-fixed w-full"}
                                             (d/thead (d/tr {:class "border-b border-t border-solid border-orange-500 bg-gray-700 text-gray-900"}
                                                            (d/th {:class "w-1/2 px-4 py-2 font-base border-l border-solid border-orange-500"} "Projekt")
                                                            (d/th {:class "w-1/2 px-4 py-2 font-base border-l border-r border-solid border-orange-500"} "Uloga")
                                                            (d/th {:class "w-1/2 px-4 py-2 font-base border-l border-r border-solid border-orange-500"} "Datum zaduzenja")))
                                             (d/tbody
                                               #_($ TableItem {:projectId     (:ProjectCode person-role)
                                                            :projectName   "Mock PROJECT name"
                                                            :roleId        (:RoleId person-role)
                                                            :assignmentDate "MOCK date"
                                                            :roleName      "Mock ROLE name"
                                                            :key     "FAKE key"
                                                            :id      "FAKE id"
                                                            &        props})
                                               #_(map (fn [p]
                                                        (= 1 1)
                                                        #_($ TableItem {:FirstName     (:FirstName p)
                                                                      :LastName (:LastName p)
                                                                      :PersonalId     (:PersonalId p)
                                                                      :key     (:id p)
                                                                      :id      (:id p)
                                                                      &        props}))
                                                    person-roles-enriched)))))))

(def PersonEdit (with-keechma Renderer))