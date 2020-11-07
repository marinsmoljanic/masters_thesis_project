(ns app.ui.main
  (:require [keechma.next.helix.core :refer [with-keechma use-sub]]
            [keechma.next.helix.lib :refer [defnc]]
            [helix.core :as hx :refer [$]]
            [helix.dom :as d]

            [app.ui.pages.home :refer [Home]]
            [app.ui.pages.uloga :refer [Uloga]]
            [app.ui.pages.projekt :refer [Projekt]]
            [app.ui.pages.osoba :refer [Osoba]]
            [app.ui.pages.uloga-osobe :refer [UlogaOsobe]]))

(defnc MainRenderer [props]
  (let [{:keys [page]} (use-sub props :router)]
    (case page
      "home" ($ Home)
      "osoba" ($ Osoba)
      "projekt" ($ Projekt)
      "uloga" ($ Uloga)
      "ulogaosobe" ($ UlogaOsobe)

      (d/div "404"))))

(def Main (with-keechma MainRenderer))