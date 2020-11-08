(ns app.ui.pages.osoba
  (:require [helix.dom :as d]
            [helix.core :as hx :refer [$]]
            [keechma.next.helix.lib :refer [defnc]]
            [keechma.next.controllers.router :as router]
            [keechma.next.helix.core :refer [with-keechma]]
            [keechma.next.helix.classified :refer [defclassified]]

            [app.ui.components.pure.shared :refer [AddNewItem]]
            [app.ui.components.header :refer [Header]]))

(def osobe
  [{:id      1
    :ime     "Marin"
    :prezime "Smoljanic"
    :OIB     "35113149930"}
   {:id      2
    :ime     "Klara"
    :prezime "Rusan"
    :OIB     "314145151532"}
   {:id      3
    :ime     "Nina"
    :prezime "Badric"
    :OIB     "351123449930"}
   {:id      4
    :ime     "Toni"
    :prezime "Ivanovic"
    :OIB     "35113149930"}
   {:id      5
    :ime     "Martin"
    :prezime "Hrastic"
    :OIB     "35113149930"}
   {:id      6
    :ime     "Mislav"
    :prezime "Vukovic"
    :OIB     "35113149930"}])

(defclassified PageWrapper :div "flex flex-col h-screen w-screen bg-gray-800 relative")

(defnc TableItem [props]
       (d/tr {:class "border-b border-solid border-gray-700"}
             (d/td {:class "pl-2 py-2 text-white"} (:ime props))
             (d/td {:class "pl-2 py-2 text-white"} (:prezime props))
             (d/td {:class "pl-2 py-2 text-white"} (:oib props))))

(defnc Table [props]
       (d/div {:class "flex h-screen w-full flex-col items-center"}
              (d/table {:class "table-fixed w-full top-0"}
                       (d/thead (d/tr {:class "border-b border-t border-solid border-orange-500 bg-gray-700 text-gray-900"}
                                      (d/th {:class "w-1/4 px-4 py-2 font-base"} "Ime")
                                      (d/th {:class "w-1/4 px-4 py-2 font-base border-l border-r border-solid border-orange-500"} "Prezime")
                                      (d/th {:class "w-2/4 px-4 py-2 font-base"} "OIB")))
                       (d/tbody
                         (map #($ TableItem {:ime     (:ime %)
                                             :prezime (:prezime %)
                                             :oib     (:OIB %)
                                             :key     (:id %)})
                              osobe)))))

(defnc Renderer [props]
       ($ PageWrapper
          ($ Header {:naslov "Osoba"})
          ($ Table)
          ($ AddNewItem)))

(def Osoba (with-keechma Renderer))