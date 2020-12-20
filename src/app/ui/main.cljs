(ns app.ui.main
  (:require [keechma.next.helix.core :refer [with-keechma use-sub]]
            [keechma.next.helix.lib :refer [defnc]]
            [helix.core :as hx :refer [$]]
            [helix.dom :as d]

            [app.ui.pages.home :refer [Home]]
            [app.ui.pages.role :refer [Role]]
            [app.ui.pages.project :refer [Project]]
            [app.ui.pages.person :refer [Person]]
            [app.ui.pages.person-role :refer [PersonRoleForm]]
            [app.ui.pages.person-edit :refer [PersonEdit]]
            [app.ui.pages.project-edit :refer [ProjectEdit]]))

(defnc MainRenderer [props]
  (let [{:keys [page]} (use-sub props :router)]
    (case page
      "home" ($ Home)
      "osoba" ($ Person)
      "projekt" ($ Project)
      "uloga" ($ Role)
      "ulogaosobe" ($ PersonRoleForm)
      "personedit" ($ PersonEdit)
      "projectedit" ($ ProjectEdit)

      (d/div "404"))))

(def Main (with-keechma MainRenderer))