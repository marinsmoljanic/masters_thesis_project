(ns app.ui.pages.person
  (:require [helix.dom :as d]
            [helix.core :as hx :refer [$]]
            [keechma.next.helix.lib :refer [defnc]]
            [keechma.next.controllers.router :as router]
            [keechma.next.helix.core :refer [with-keechma dispatch call use-sub]]
            [keechma.next.helix.classified :refer [defclassified]]
            [keechma.next.controllers.router :refer [redirect!]]

            [app.ui.components.header :refer [Header HeaderCreateForm]]
            [app.ui.components.forms.osoba :refer [PersonForm]]))

(defclassified PageWrapper :div "flex flex-col h-screen w-screen bg-gray-800 relative")

(defnc TableItem [props]
       (d/tr {:class "border-b border-solid border-gray-700 cursor-pointer hover:bg-gray-900"
              :on-click #(redirect! props :router {:page "personedit"
                                                   :id (:id props)})}
             (d/td {:class "pl-2 py-2 text-white"} (:FirstName props))
             (d/td {:class "pl-2 py-2 text-white"} (:LastName props))
             (d/td {:class "pl-2 py-2 text-white"} (:PersonalId props))))

(defnc Renderer [props]
       (let [is-form-open? (get-in (use-sub props :person-form) [:is-form-open?])
             persons       (get-in (use-sub props :persons) [:allPerson])]
       ($ PageWrapper
          (if is-form-open?
            ($ HeaderCreateForm {:naslov "Lista osoba"})
            ($ Header {:naslov "Lista osoba"}))

          (if is-form-open?
            ($ PersonForm)

            (d/div {:class "flex h-screen w-full flex-col items-center"}
                   (d/table {:class "table-fixed w-full top-0"}
                            (d/thead (d/tr {:class "border-b border-t border-solid border-orange-500 bg-gray-700 text-gray-900"
                                            }
                                           (d/th {:class "w-1/4 px-4 py-2 font-base"} "Ime")
                                           (d/th {:class "w-1/4 px-4 py-2 font-base border-l border-r border-solid border-orange-500"} "Prezime")
                                           (d/th {:class "w-2/4 px-4 py-2 font-base"} "OIB")))
                            (d/tbody
                              (map (fn [p]
                                        ($ TableItem {:FirstName     (:FirstName p)
                                                      :LastName (:LastName p)
                                                      :PersonalId     (:PersonalId p)
                                                      :key     (:id p)
                                                      :id      (:id p)
                                                      &        props}))
                                   persons)))))

          (d/div {:class "flex justify-end py-8 px-8 absolute bottom-0 w-full"}
                 (d/button {:class (str (if is-form-open? "bg-transparent border-solid border-2 border-red-600 " "bg-orange-600 ") "rounded-full text-white h-20 w-20 justify-center items-center text-4xl font-thin focus:outline-none")
                            :on-click #(dispatch props :person-form :toggle nil)}
                           (if is-form-open? "x" "+"))))))

(def Person (with-keechma Renderer))