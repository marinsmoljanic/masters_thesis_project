(ns app.ui.components.forms.projekt
  (:require [helix.dom :as d]
            [helix.core :as hx :refer [$]]
            [keechma.next.helix.lib :refer [defnc]]
            [keechma.next.controllers.router :as router]
            [keechma.next.helix.core :refer [with-keechma dispatch]]
            [keechma.next.helix.classified :refer [defclassified]]

            [app.ui.components.inputs :refer [wrapped-input]]

            [app.ui.components.header :refer [Header]]))


(defnc RenderErrors [{:keys [error] :as props}]
       (d/div {:class "text-redDark text-xs pt-2"}
              error))


(defnc Renderer [props]
       (d/div {:class "m-auto min-w-full mt-8 px-4 text-white"}
              (d/form {:on-submit (fn [e]
                                      (.preventDefault e)
                                      (dispatch props :project-form :keechma.form/submit))}

                      (d/p {:class "text-sm text-grayLight text-left w-full mb-6"} "Naziv projekta")
                      (wrapped-input {:keechma.form/controller :project-form
                                      :input/type              :text
                                      :input/attr              :name
                                      :placeholder             "Naziv projekta"})

                      (d/div {:class "max-width-full h-6 transparent"})
                      (d/p {:class "text-sm text-grayLight text-left w-full mb-6"} "Opis projekta")
                      (wrapped-input {:keechma.form/controller :project-form
                                      :input/type              :text
                                      :input/attr              :description
                                      :placeholder             "Opis projekta"})
                      (d/p {:class "text-sm text-grayLight text-left w-full mb-6"} "Datum pocetka")
                      (wrapped-input {:keechma.form/controller :project-form
                                      :input/type              :text
                                      :input/attr              :startdate
                                      :placeholder             "Datum pocetka"})

                      (d/div {:class "max-width-full h-6 transparent"})
                      (d/p {:class "text-sm text-grayLight text-left w-full mb-6"} "Datum zavrsetka")
                      (wrapped-input {:keechma.form/controller :project-form
                                      :input/type              :text
                                      :input/attr              :ednddate
                                      :placeholder             "Datum zavrsetka"})

                      #_(when error ($ RenderErrors {:error error}))

                      (d/button {:class "block margin-auto mx-auto border w-56 px-4 py-3 rounded-sm
                                             text-md font-medium text-white bg-transparent hover:bg-gray-700"
                                 :on-click #(dispatch props :project-form :toggle nil)} "Spremi")
                      )))

(def ProjectForm (with-keechma Renderer))