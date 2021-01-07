(ns app.ui.pages.role
  (:require [helix.dom :as d]
            [helix.core :as hx :refer [$]]
            [keechma.next.helix.lib :refer [defnc]]
            [keechma.next.toolbox.logging :as l]
            [keechma.next.controllers.router :as router]
            [keechma.next.helix.core :refer [with-keechma dispatch use-sub use-meta-sub]]
            [keechma.next.helix.classified :refer [defclassified]]
            [app.ui.components.inputs :refer [wrapped-input input]]
            [app.ui.components.header :refer [Header]]))

(defclassified PageWrapper :div "flex flex-col h-screen w-screen bg-gray-800")

(defnc RoleItemForm [{:keys [form-id] :as props}]
       (let [form-ident [:role-edit-form form-id]
             meta-state (use-meta-sub props form-ident)]
             (d/div {:class "w-full pt-4 px-4 text-white border-b border-gray-700 border-solid"}
                    (d/form {:on-submit (fn [e]
                                           (.preventDefault e)
                                            (dispatch props form-ident :keechma.form/submit))}
                           (d/div {:class "flex flex-row w-full"}
                               (wrapped-input {:keechma.form/controller form-ident
                                               :input/type              :textPlain
                                               :input/attr              :Name
                                               :placeholder             "Naziv Uloge"})

                               (d/div {:class "flex flex-row justify-between px-10"}
                                      (d/button {:class "block margin-auto mb-4 px-2 rounded-sm border-gray-600
                                                         text-md font-medium text-white bg-transparent hover:bg-gray-900
                                                         border hover:border-red-600 focus:outline-none"
                                                 :type "button"
                                                 :on-click #(dispatch props form-ident :delete form-id)} "Obrisi")

                                      (d/button {:class "block margin-auto mb-4 px-2 rounded-sm border-gray-600 ml-2
                                                         text-md font-medium text-white bg-transparent hover:bg-gray-900
                                                         border hover:border-green-600 focus:outline-none"} "Spremi")))))))

(defnc Renderer [props]
       (let [roles (use-sub props :roles)
             data (use-sub props :some-data)]
         ($ PageWrapper
          ($ Header {:naslov "Šifrarnik uloga"})
          (d/div {:class "flex w-full flex-col items-center"}
                 (when roles
                       (map (fn [{:keys [id]}]
                                ($ RoleItemForm {:form-id id
                                                 :key     id
                                                 &        props}))
                            roles)))

          (d/div {:class "w-full pt-4 px-4 text-white"}
                 (d/form {:on-submit (fn [e]
                                         (.preventDefault e)
                                         (dispatch props :role-form :keechma.form/submit))}

                         (d/div {:class "flex flex-row w-full"}
                                (wrapped-input {:keechma.form/controller :role-form
                                                :input/type              :text
                                                :input/attr              :name
                                                :placeholder             "Naziv nove uloge"})

                                (d/div {:class "flex flex-row justify-between px-10"}
                                       (d/button {:class "block margin-auto mb-4 px-2 rounded-sm border-gray-600
                                                                        text-md font-medium text-white bg-transparent hover:bg-gray-900
                                                                        border hover:border-green-600 focus:outline-none"} "Spremi"))))))))

(def Role (with-keechma Renderer))