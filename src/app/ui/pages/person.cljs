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

#_(defclassified PageWrapper :div "flex flex-col h-screen w-screen bg-gray-800 relative")
(defclassified PageWrapper :div "flex flex-col h-screen w-screen bg-gray-800
                                 md:h-full md:mx-auto md:w-1/2 shadow")

(defnc TableItem [props]
       (d/tr {:class "border-b border-solid border-gray-700 cursor-pointer hover:bg-gray-900"
              :on-click #(redirect! props :router {:page      "personedit"
                                                   :id        (:id props)
                                                   :firstName (:FirstName props)
                                                   :lastName  (:LastName props)})}
             (d/td {:class "pl-2 py-2 text-white"} (:FirstName props))
             (d/td {:class "pl-2 py-2 text-white"} (:LastName props))
             (d/td {:class "pl-2 py-2 text-white"} (:PersonalId props))))

(defnc Renderer [props]
       (let [is-form-open? (get-in (use-sub props :person-form) [:is-form-open?])
             persons       (get-in (use-sub props :persons) [:allPerson])]
       ($ PageWrapper
          (if is-form-open?
            ($ HeaderCreateForm {:naslov "Unesite podatke nove osobe"})
            ($ Header {:naslov "Lista osoba"}))

          (if is-form-open?
            (d/div {:class "md:pb-8 md:border-solid md:border-b-2 md:border-l md:border-r md:border-orange-400"}
                  ($ PersonForm)
                  (d/div {:class "flex justify-end py-8 px-8 absolute w-full
                                  md:w-1/2 md:mt-8"}
                         (d/button {:class "bg-transparent border-solid border-2 border-red-600 rounded-full text-white h-20 w-20 justify-center items-center text-4xl font-thin focus:outline-none
                                            md:bg-gray-600 md:justify-end md:border-red-800"
                                    :on-click #(dispatch props :person-form :toggle nil)}
                                   "x")))

            (d/div {:class "flex w-full flex-col items-center
                            md:border-solid md:border-b-2 md:border-l md:border-r md:border-orange-400"}
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
                                   persons))
                            (d/div {:class "flex justify-end py-8 px-8 absolute w-full
                                            md:w-1/2"}
                                   (d/button {:class (str (if is-form-open? "bg-transparent border-solid border-2 border-red-600 " "bg-orange-600 ") "rounded-full text-white h-20 w-20 justify-center items-center text-4xl font-thin focus:outline-none
                                                                             md:bg-orange-600")
                                              :on-click #(dispatch props :person-form :toggle nil)}
                                             (if is-form-open? "x" "+"))
                                   )))))))

(def Person (with-keechma Renderer))