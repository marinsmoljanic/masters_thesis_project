(ns app.ui.pages.person-role
  (:require [helix.dom :as d]
            [helix.core :as hx :refer [$]]
            [keechma.next.helix.lib :refer [defnc]]
            [keechma.next.controllers.router :as router]
            [keechma.next.helix.core :refer [with-keechma dispatch]]
            [keechma.next.helix.classified :refer [defclassified]]
            [keechma.next.helix.core :refer [with-keechma use-meta-sub dispatch call use-sub]]

            [app.ui.components.inputs :refer [wrapped-input]]

            [app.ui.components.header :refer [Header]]))


(defclassified PageWrapper :div "flex flex-col h-screen w-screen bg-gray-800 relative")

(defnc RenderErrors [{:keys [error] :as props}]
       (d/div {:class "text-redDark text-xs pt-2"}
              error))


(defnc Renderer [props]
       (let [persons (get-in (use-sub props :persons) [:allPerson])
             roles (get-in (use-sub props :roles) [:allRole])
             projects (get-in (use-sub props :projects) [:allProject])
             _ (println "PERSONS: " persons)
             _ (println "ROLES: " roles)
             _ (println "PROJECTS: " projects)
             _ (println "______________________________________________________________")]
            ($ PageWrapper
               ($ Header {:naslov "Dodjela zaduzenja"})

               (d/div {:class "m-auto min-w-full mt-8 px-4 text-white"}
                        (d/form {:on-submit (fn [e]
                                                (.preventDefault e)
                                                (dispatch props :person-role-form :keechma.form/submit))}

                                (d/p {:class "text-sm text-grayLight text-left w-full mb-6"} "Projekt")
                                (wrapped-input {:input/type :select
                                                :input/attr :projectcode
                                                :value      "HardCode"
                                                :options    [{:value "ALL" :label "ALL"}
                                                             {:value "ALERT" :label "Alert"}
                                                             {:value "SENT_COMMAND COMMAND" :label "Command"}
                                                             {:value "COMMAND" :label "Command Response"}
                                                             {:value "EXCEPTION" :label "Exception"}
                                                             {:value "NORMAL" :label "Normal"}]
                                                ;; :on-change #(dispatch props :tracking-filter :tracking-filter-category (oget % :target.value))
                                                :on-change #(println "Desi lesi")
                                                })

                                (d/p {:class "text-sm text-grayLight text-left w-full mb-6 pt-6"} "Osoba")
                                (wrapped-input {:input/type :select
                                                :input/attr :personid
                                                :value      "HardCode"
                                                :options    [{:value "ALL" :label "ALL"}
                                                             {:value "ALERT" :label "Alert"}
                                                             {:value "SENT_COMMAND COMMAND" :label "Command"}
                                                             {:value "COMMAND" :label "Command Response"}
                                                             {:value "EXCEPTION" :label "Exception"}
                                                             {:value "NORMAL" :label "Normal"}]
                                                ;; :on-change #(dispatch props :tracking-filter :tracking-filter-category (oget % :target.value))
                                                :on-change #(println "Desi lesi")
                                                })

                                (d/p {:class "text-sm text-grayLight text-left w-full mb-6 pt-6"} "Uloga")
                                (wrapped-input {:input/type :select
                                                :input/attr :roleid
                                                :value      "HardCode"
                                                :options    [{:value "ALL" :label "ALL"}
                                                             {:value "ALERT" :label "Alert"}
                                                             {:value "SENT_COMMAND COMMAND" :label "Command"}
                                                             {:value "COMMAND" :label "Command Response"}
                                                             {:value "EXCEPTION" :label "Exception"}
                                                             {:value "NORMAL" :label "Normal"}]
                                                ;; :on-change #(dispatch props :tracking-filter :tracking-filter-category (oget % :target.value))
                                                :on-change #(println "Desi lesi")
                                                })

                                (d/p {:class "text-sm text-grayLight text-left w-full mb-6 pt-6"} "Datum dodjele")
                                (wrapped-input {:keechma.form/controller :person-form
                                                :input/type              :text
                                                :input/attr              :assignmentdate
                                                :placeholder             "DD/MM/GG"})

                                #_(when error ($ RenderErrors {:error error}))
                                (d/div {:class "w-full mt-8"}
                                       (d/button {:class    "block margin-auto mx-auto border w-56 px-4 py-3 rounded-sm
                                                       text-md font-medium text-white bg-transparent hover:bg-gray-700"
                                                  :on-click #(dispatch props :person-form :toggle nil)} "Spremi zaduzenje"))
                                )))))

(def PersonRoleForm (with-keechma Renderer))