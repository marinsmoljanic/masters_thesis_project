(ns app.app
  (:require ["react-dom" :as rdom]
            [app.controllers.forms.role]
            [app.controllers.forms.person]
            [app.controllers.forms.project]
            [app.controllers.forms.person-role]

            [app.controllers.forms.person-edit]
            [app.controllers.forms.project-edit]
            [app.controllers.forms.role-edit]

            [keechma.next.toolbox.logging :as l]

            [keechma.next.controllers.router]
            [keechma.next.controllers.entitydb]
            [keechma.next.controllers.dataloader]
            [keechma.next.controllers.subscription]


            [app.controllers.some-form]
            [app.controllers.some-data]

            [app.controllers.datasources.role]
            [app.controllers.datasources.person]
            [app.controllers.datasources.project]
            [app.controllers.datasources.person-role]
            [app.controllers.datasources.person-role-by-personid]))

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

       ;; ROLE
       :roles                   #:keechma.controller {:params (fn [{:keys [router]}]
                                                                  (or (page-equal? router "uloga")
                                                                      (page-equal? router "ulogaosobe")
                                                                      (page-equal? router "personedit")))
                                                      :deps   [:router :entitydb]}

       [:role-edit-form]        {:keechma.controller.factory/produce
                                              (fn [{:keys [roles]}]
                                                  (let [roles (:allRole roles)]
                                                      (->> (map (fn [role]
                                                                  [(js/parseInt (:id role)) {:keechma.controller/params role}])
                                                                roles)
                                                           (into {}))))
                                 :keechma.controller/deps [:roles]}

       :some-data   {:keechma.controller/params true}
       [:some-form] {:keechma.controller.factory/produce (fn [{:keys [some-data]}]
                                                             (->> some-data
                                                                  (map (fn [data] [(:id data) {:keechma.controller/params data}]))
                                                                  (into {})))
                     :keechma.controller/deps            [:some-data]}


       :role-form               #:keechma.controller {:params (fn [{:keys [router]}]
                                                                  (or (page-equal? router "uloga")
                                                                      (page-equal? router "ulogaosobe")))
                                                      :deps   [:router :entitydb]}

       ;; PERSON
       :person-form             {:keechma.controller/params true}
       :persons                 #:keechma.controller {:params (fn [{:keys [router]}]
                                                                  (or (page-equal? router "osoba")
                                                                      (page-equal? router "ulogaosobe")))
                                                      :deps   [:router :entitydb]}
       :person-edit-form        #:keechma.controller {:params (fn [{:keys [router]}]
                                                                  (contains-page-id? router))
                                                      :deps   [:router :entitydb]}

       ;; PROJECT
       :projects                #:keechma.controller {:params (fn [{:keys [router]}]
                                                                  (or (page-equal? router "projekt")
                                                                      (page-equal? router "ulogaosobe")
                                                                      (page-equal? router "personedit")))
                                                      :deps   [:router :entitydb]}
       :project-form            #:keechma.controller {:params (fn [{:keys [router]}]
                                                                  (page-equal? router "projekt"))
                                                      :deps   [:router]}
       :project-edit-form       #:keechma.controller {:params (fn [{:keys [router]}]
                                                                  (contains-page-id? router))
                                                      :deps   [:router :entitydb]}

       ;; PERSON ROLE
       :person-roles            #:keechma.controller {:params (fn [{:keys [router]}]
                                                                  (page-equal? router "ulogaosobe"))
                                                      :deps   [:router :entitydb]}
       :person-role-form        #:keechma.controller {:params (fn [{:keys [router]}]
                                                                  (page-equal? router "ulogaosobe"))
                                                      :deps   [:router :entitydb]}

       ;; UTILITY
       :person-role-by-personid #:keechma.controller {:params (fn [{:keys [router]}]
                                                                  (contains-page-id? router))
                                                      :deps   [:router :entitydb :roles :projects]}

       }})
