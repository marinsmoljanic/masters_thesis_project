(ns app.ui.components.forms.osoba
  (:require [helix.dom :as d]
            [helix.core :as hx :refer [$]]
            [keechma.next.helix.lib :refer [defnc]]
            [keechma.next.controllers.router :as router]
            [keechma.next.helix.core :refer [with-keechma dispatch]]
            [keechma.next.helix.classified :refer [defclassified]]
            [keechma.next.helix.core :refer [with-keechma use-meta-sub dispatch call use-sub]]

            [app.ui.components.inputs :refer [wrapped-input]]

            [app.ui.components.header :refer [Header]]))

(defnc RenderErrors [{:keys [error] :as props}]
       (d/div {:class "text-redDark text-xs pt-2"}
              error))


(defnc Renderer [props]
       (d/div {:class "m-auto min-w-full mt-8 px-4 text-white"}
              (d/form {:on-submit (fn [e]
                                      (.preventDefault e)
                                      (dispatch props :person-form :keechma.form/submit))}

                      (d/p {:class "text-sm text-grayLight text-left w-full mt-6"} "Ime osobe")
                      (wrapped-input {:keechma.form/controller :person-form
                                      :input/type              :text
                                      :input/attr              :name
                                      :placeholder             "Ime"})

                      (d/p {:class "text-sm text-grayLight text-left w-full mt-6"} "Prezime osobe")
                      (wrapped-input {:keechma.form/controller :person-form
                                      :input/type              :text
                                      :input/attr              :surname
                                      :placeholder             "Prezime"})

                      (d/p {:class "text-sm text-grayLight text-left w-full mt-6"} "OIB")
                      (wrapped-input {:keechma.form/controller :person-form
                                      :input/type              :text
                                      :input/attr              :surname
                                      :placeholder             "OIB"})

                      (d/button {:class "block margin-auto mx-auto border w-56 px-4 py-3 rounded-sm
                                             text-md font-medium text-white bg-transparent hover:bg-gray-700"
                                 :on-click #(dispatch props :person-form :toggle nil)} "Spremi")
                     )))

(def PersonForm (with-keechma Renderer))