(ns app.ui.pages.person-role
  (:require [helix.dom :as d]
            [helix.core :as hx :refer [$]]
            [keechma.next.helix.lib :refer [defnc]]
            [keechma.next.controllers.router :as router]
            [keechma.next.helix.core :refer [with-keechma dispatch]]
            [keechma.next.helix.classified :refer [defclassified]]
            [keechma.next.toolbox.logging :as l]
            [keechma.next.helix.core :refer [with-keechma use-meta-sub dispatch call use-sub]]

            [app.ui.components.inputs :refer [wrapped-input]]

            [app.ui.components.header :refer [Header]]))


(defclassified PageWrapper :div "flex flex-col h-screen w-screen bg-gray-800 relative")

(defnc RenderErrors [{:keys [error] :as props}]
       (d/div {:class "text-redDark text-xs pt-2"}
              error))


(defnc Renderer [props]
       (let [persons (get-in (use-sub props :persons) [:allPerson])
             persons-map {}
             persons-select-value (map (fn [person]
                                           (assoc persons-map :label (str (:FirstName person) " " (:LastName person)) :value (:id person)))
                                       persons)

             roles (get-in (use-sub props :roles) [:allRole])
             roles-map {}
             roles-select-value (map (fn [role]
                                           (assoc roles-map :label (:Name role) :value (:id role)))
                                       roles)

             projects (get-in (use-sub props :projects) [:allProject])
             projects-map {}
             projects-select-value (map (fn [project]
                                         (assoc projects-map :label (:Name project) :value (:id project)))
                                     projects)
             ;; _ (l/pp "PERSONS: " persons)
             ;; _ (l/pp "ROLES: " roles)
             ;; _ (l/pp "PROJECTS: " projects)
             ;; _ (l/pp "Persons select value: " persons-select-value)
             ;; _ (l/pp "Roles select value: " roles-select-value)
             ;; _ (l/pp "Projects select value: " projects-select-value)
             ;; _ (l/pp "______________________________________________________________")]
             ]
            ($ PageWrapper
               ($ Header {:naslov "Dodjela zaduzenja"})

               (d/div {:class "m-auto min-w-full mt-8 px-4 text-white"}
                        (d/form {:on-submit (fn [e]
                                                (.preventDefault e)
                                                (dispatch props :person-role-form :keechma.form/submit))}

                                (d/p {:class "text-sm text-grayLight text-left w-full mb-6"} "Projekt")
                                (wrapped-input {:keechma.form/controller :person-role-form
                                                :input/type :select
                                                :input/attr :project
                                                :options    projects-select-value
                                                ;; :on-change #(dispatch props :tracking-filter :tracking-filter-category (oget % :target.value))
                                                ;;:on-change #(println "Desi lesi")
                                                })

                                (d/p {:class "text-sm text-grayLight text-left w-full mb-6 pt-6"} "Osoba")
                                (wrapped-input {:keechma.form/controller :person-role-form
                                                :input/type :select
                                                :input/attr :person
                                                :options    persons-select-value
                                                ;; :on-change #(dispatch props :tracking-filter :tracking-filter-category (oget % :target.value))
                                                ;;:on-change #(println "Desi lesi")
                                                })

                                (d/p {:class "text-sm text-grayLight text-left w-full mb-6 pt-6"} "Uloga")
                                (wrapped-input {:keechma.form/controller :person-role-form
                                                :input/type :select
                                                :input/attr :role
                                                :options    roles-select-value
                                                ;; :on-change #(dispatch props :tracking-filter :tracking-filter-category (oget % :target.value))
                                                ;; :on-change #(println "Desi lesi")
                                                })

                                (d/p {:class "text-sm text-grayLight text-left w-full mb-6 pt-6"} "Datum dodjele")
                                (wrapped-input {:keechma.form/controller :person-role-form
                                                :input/type              :text
                                                :input/attr              :date
                                                :placeholder             "DD/MM/GG"})

                                #_(when error ($ RenderErrors {:error error}))
                                (d/div {:class "w-full mt-8"}
                                       (d/button {:class    "block margin-auto mx-auto border w-56 px-4 py-3 rounded-sm
                                                       text-md font-medium text-white bg-transparent hover:bg-gray-700"} "Spremi zaduzenje"))
                                )))))

(def PersonRoleForm (with-keechma Renderer))