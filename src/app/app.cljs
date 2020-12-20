(ns app.app
  (:require [app.controllers.datasources.role]
            [app.controllers.datasources.person]
            [app.controllers.datasources.project]
            [app.controllers.datasources.person-role]
            [app.controllers.datasources.person-role-by-personid]

            [keechma.next.controllers.router]
            [keechma.next.controllers.entitydb]
            [keechma.next.controllers.dataloader]
            [keechma.next.controllers.subscription]

            [app.controllers.forms.person]
            [app.controllers.forms.project]
            [app.controllers.forms.person-role]
            [app.controllers.forms.person-edit]
            [app.controllers.forms.project-edit]

            ["react-dom" :as rdom]))


(defn page-equal? [router page] (let [route-page (:page router)] (= route-page page)))
(defn page-eq? [page] (fn [{:keys [router]}] (= page (:page router))))
(defn contains-page-id? [router] (and (:page router) (:id router)))
(defn role-eq? [role] (fn [deps] (= role (:role deps))))
(defn slug [{:keys [router]}] (:slug router))
(def homepage? (page-eq? "home"))


(def app
  {:keechma.subscriptions/batcher rdom/unstable_batchedUpdates,
   :keechma/controllers
      {:router                  {:keechma.controller/params true,
                                 :keechma.controller/type   :keechma/router,
                                 :keechma/routes            [[""
                                                              {:page "home"}]
                                                             ":page"
                                                             ":page/:id"]},

       :dataloader              {:keechma.controller/params true,
                                 :keechma.controller/type   :keechma/dataloader}

       :entitydb                {:keechma.controller/params true
                                 :keechma.controller/type   :keechma/entitydb}

       :person-form             {:keechma.controller/params true}
       :project-form            #:keechma.controller {:params (fn [{:keys [router]}]
                                                                  (page-equal? router "projekt"))
                                                      :deps   [:router]}

       :persons                 #:keechma.controller {:params (fn [{:keys [router]}]
                                                                  (or (page-equal? router "osoba")
                                                                      (page-equal? router "ulogaosobe")))
                                                      :deps   [:router :entitydb]}

       :projects                #:keechma.controller {:params (fn [{:keys [router]}]
                                                                  (or (page-equal? router "projekt")
                                                                      (page-equal? router "ulogaosobe")))
                                                      :deps   [:router :entitydb]}

       :roles                   #:keechma.controller {:params (fn [{:keys [router]}]
                                                                  (or (page-equal? router "uloga")
                                                                      (page-equal? router "ulogaosobe")))
                                                      :deps   [:router :entitydb]}

       :person-roles            #:keechma.controller {:params (fn [{:keys [router]}]
                                                                  (page-equal? router "ulogaosobe"))
                                                      :deps   [:router :entitydb]}

       :person-role-form        #:keechma.controller {:params (fn [{:keys [router]}]
                                                                  (page-equal? router "ulogaosobe"))
                                                      :deps   [:router :entitydb]}

       :person-edit-form        #:keechma.controller {:params (fn [{:keys [router]}]
                                                                  (contains-page-id? router))
                                                      :deps   [:router :entitydb]}

       :project-edit-form       #:keechma.controller {:params (fn [{:keys [router]}]
                                                                  (contains-page-id? router))
                                                      :deps   [:router :entitydb]}

       :person-role-by-personid #:keechma.controller {:params (fn [{:keys [router]}]
                                                                  (contains-page-id? router))
                                                      :deps   [:router :entitydb]}

       }
}
  )
