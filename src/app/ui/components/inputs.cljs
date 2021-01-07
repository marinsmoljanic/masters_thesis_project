(ns app.ui.components.inputs
  (:require [keechma.next.helix.core :refer
             [with-keechma use-meta-sub dispatch]]
            [keechma.next.helix.lib :refer [defnc]]
            [helix.core :as hx :refer [$ <> suspense]]
            [helix.dom :as d]
            [helix.hooks :as hooks]
            [oops.core :refer [ocall oget oset!]]
            ["react" :as react]
            ["react-dom" :as rdom]
            [keechma.next.controllers.form :as form]
            [app.validators :refer [get-validator-message]]))

(defn get-element-props
      [default-props props]
      (let [element-props (into {} (filter (fn [[k v]] (simple-keyword? k)) props))]
           (reduce-kv
             (fn [m k v]
                 (let [prev-v (get k m)
                       val (cond (and (fn? prev-v) (fn? v))
                                 (fn [& args] (apply prev-v args) (apply v args))
                                 (and (= :class k) (:class m)) (flatten [v (:class m)])
                                 :else v)]
                      (assoc m k val)))
             default-props
             element-props)))

;; ERRORS
(defnc ErrorsRenderer [{:keechma.form/keys [controller]
                        :input/keys        [attr]
                        :as                props}]
       (let [errors-getter (hooks/use-callback [attr] #(form/get-errors-in % attr))
             errors (use-meta-sub props controller errors-getter)]
            (when-let [errors' (get-in errors [:$errors$ :failed])]
                      (d/ul {:class "error-messages  text-redDark py-4 max-width-full text-xs error-border-color:parent"}
                            (map-indexed (fn [i e] (d/li {:key i} (get-validator-message e)))
                                         errors')))))

(def Errors (with-keechma ErrorsRenderer))

;; PASSWORD
(defnc PasswordInputRenderer [{:keechma.form/keys [controller]
                               :input/keys        [attr]
                               :as                props}]
       (let [element-props (get-element-props {} props)
             value-getter (hooks/use-callback [attr] #(form/get-data-in % attr))
             value (use-meta-sub props controller value-getter)
             password-input-element (or (:id props) "pass")
             pass-input-ele (js/document.getElementById password-input-element)]
            (d/div {:class "relative w-full"}
                   (d/div {:class "absolute top-0 right-0 flex h-full items-center"
                           :style {:height "calc(100% - 2px)"}}
                          (d/i {:class    (str "fal fa-eye-slash text-lg text-grayLight pb-2")
                                :on-click (fn [] (oset! pass-input-ele "type" (if (= "text" (oget pass-input-ele "type"))
                                                                                "password"
                                                                                "text")))}))
                   (when (:prepend/icon props)
                         (d/div {:class "absolute top-0 left-0 flex items-center pb-3"
                                 :style {:height "calc(100% - 2px)"}}
                                (d/i {:class (str "fal " (:prepend/icon props))})))
                   (when (:iconend props)
                         (d/div {:class "absolute top-0 right-0 flex h-full items-center mr-8"}
                                (d/i {:class (str "fal mb-3 text-2xl text-green " (:iconend props))})))
                   (d/input
                     {:value     (str value)
                      :id        (or (:id props) "pass")
                      :disabled  (:disable props)
                      :type      "password"
                      :on-change #(dispatch props
                                            controller
                                            :keechma.form.on/change
                                            {:value (.. % -target -value) :attr attr})
                      :on-blur   #(dispatch props
                                            controller
                                            :keechma.form.on/blur
                                            {:value (.. % -target -value) :attr attr})
                      &          element-props}))))
(def PasswordInput (with-keechma PasswordInputRenderer))

;; TEXT
(defnc TextInputRenderer [{:keechma.form/keys [controller]
                           :input/keys        [attr]
                           :as                props}]
       (let [element-props (get-element-props {} props)
             value-getter (hooks/use-callback [attr] #(form/get-data-in % attr))
             value (use-meta-sub props controller value-getter)]
            (d/div {:class "relative w-full"}
                   (when (:prepend/icon props)
                         (d/div {:class "absolute top-0 left-0 flex pb-3 items-center"
                                 :style {:height "calc(100% - 2px)"}}
                                (d/i {:class (str "fal text-grayLight " (:prepend/icon props))})))
                   (when (:iconend props)
                         (d/div {:class "absolute top-0 right-0 flex h-full items-center"}
                                (d/i {:class (str "fal mb-3 text-2xl text-green " (:iconend props))})))
                   (d/input
                     {:value     (str value)
                      :disabled (:disable props)
                      :on-change #(dispatch props
                                            controller
                                            :keechma.form.on/change
                                            {:value (.. % -target -value) :attr attr})
                      :on-blur   #(dispatch props
                                            controller
                                            :keechma.form.on/blur
                                            {:value (.. % -target -value) :attr attr})
                      &          element-props}))))
(def TextInput (with-keechma TextInputRenderer))

;; PLAIN-TEXT
(defnc PlainTextInputRenderer [{:keechma.form/keys [controller]
                           :input/keys        [attr]
                           :as                props}]
       (let [element-props (get-element-props {} props)
             value-getter (hooks/use-callback [attr] #(form/get-data-in % attr))
             value (use-meta-sub props controller value-getter)]
            (d/div {:class "relative w-full"}
                   (when (:prepend/icon props)
                         (d/div {:class "absolute top-0 left-0 flex pb-3 items-center"
                                 :style {:height "calc(100% - 2px)"}}
                                (d/i {:class (str "fal text-grayLight " (:prepend/icon props))})))
                   (when (:iconend props)
                         (d/div {:class "absolute top-0 right-0 flex h-full items-center"}
                                (d/i {:class (str "fal mb-3 text-2xl text-green " (:iconend props))})))
                   (d/input
                     {:value     (str value)
                      :disabled (:disable props)
                      :on-change #(dispatch props
                                            controller
                                            :keechma.form.on/change
                                            {:value (.. % -target -value) :attr attr})
                      :on-blur   #(dispatch props
                                            controller
                                            :keechma.form.on/blur
                                            {:value (.. % -target -value) :attr attr})
                      &          element-props}))))
(def PlainTextInput (with-keechma TextInputRenderer))

;; DATE
(defnc DateInputRenderer [{:keechma.form/keys [controller]
                          :input/keys        [attr]
                          :as                props}]
       (let [element-props (get-element-props {} props)
             value-getter (hooks/use-callback [attr] #(form/get-data-in % attr))
             value (use-meta-sub props controller value-getter)]
            (d/div {:class "relative w-full flex flex-col justify-start"}
                   (when (:label props)
                         (d/label {:class "text-sm text-grayLight text-grayLight w-full mb-3"
                                   :for (:label props)}
                                  (:label props)
                                  (d/div {:class "mt-2 w-6 rounded-md  h-6 bg-white flex justify-center items-center"}
                                         (when value
                                               (d/i {:class "fal fa-check text-2xl text-green"})))))
                   (d/input
                     {:type      "date"
                      :id        (:label props)
                      :name      "my-date"
                      :on-change #(dispatch props
                                            controller
                                            :keechma.form.on/change
                                            {:value (.. % -target -value) :attr attr})
                      #_(dispatch props
                                            controller
                                            :keechma.form.on/change
                                            {:value (.. % -target -checked) :attr attr})
                      &          element-props}))))
(def DateInput (with-keechma DateInputRenderer))

;; TEXT-AREA
(defnc TextAreaRenderer [{:keechma.form/keys [controller]
                          :input/keys        [attr]
                          :as                props}]
       (let [element-props (get-element-props {} props)
             value-getter (hooks/use-callback [attr] #(form/get-data-in % attr))
             value (use-meta-sub props controller value-getter)]
            (d/textarea
              {:value     (str value)
               :on-change #(dispatch props
                                     controller
                                     :keechma.form.on/change
                                     {:value (.. % -target -value) :attr attr})
               :on-blur   #(dispatch props
                                     controller
                                     :keechma.form.on/blur
                                     {:value (.. % -target -value) :attr attr})
               &          element-props})))
(def TextArea (with-keechma TextAreaRenderer))

;; CHECKBOX
(defnc CheckboxRenderer [{:keechma.form/keys [controller]
                          :input/keys        [attr]
                          :as                props}]
       (let [element-props (get-element-props {} props)
             value-getter (hooks/use-callback [attr] #(form/get-data-in % attr))
             value (use-meta-sub props controller value-getter)]
            (d/div {:class "relative w-full flex flex-col justify-start"}
                   (when (:label props)
                         (d/label {:class "text-sm text-grayLight text-grayLight w-full mb-3"
                                   :for (:label props)}
                                  (:label props)
                                  (d/div {:class "mt-2 w-6 rounded-md  h-6 bg-white flex justify-center items-center"}
                                         (when value
                                               (d/i {:class "fal fa-check text-2xl text-green"})))))
                   (d/input
                     {:type      "checkbox"
                      :id        (:label props)
                      :checked     (or value false)
                      :on-change #(dispatch props
                                            controller
                                            :keechma.form.on/change
                                            {:value (.. % -target -checked) :attr attr})
                      &          element-props}))))
(def Checkbox (with-keechma CheckboxRenderer))

;; SELECT
(defnc SelectInputRenderer [{:keechma.form/keys [controller]
                             :input/keys        [attr]
                             :as                props}]
       (let [element-props (get-element-props {} props)
             value-getter (hooks/use-callback [attr] #(form/get-data-in % attr))
             value (use-meta-sub props controller value-getter)]
            (let [{:keys [options optgroups placeholder]} props]
                 (d/div {:class "relative w-full text-gray2 pb-3"}
                        (when (:prepend/icon props)
                              (d/div {:class "absolute top-0 right-0 flex h-full items-center pb-1"}
                                     (d/div {:class "w-5 h-5 mb-2"
                                             :style {
                                                     :background-repeat "no-repeat"
                                                     :background-size "contain"}})))
                        (d/select {:key       value
                                   :value     (str value)
                                   :on-change #(dispatch props
                                                         controller
                                                         :keechma.form.on/change
                                                         {:value (.. % -target -value) :attr attr})
                                   :on-blur   #(dispatch props
                                                         controller
                                                         :keechma.form.on/blur
                                                         {:value (.. % -target -value) :attr attr})
                                   &          element-props}
                                  (when placeholder
                                        (<>
                                          (d/option {:value ""} placeholder)
                                          (d/option {:value ""} "-")))
                                  (if optgroups
                                    (map
                                      (fn [{:keys [label options]}]
                                          (when (seq options)
                                                (d/optgroup {:label label :key label}
                                                            (map
                                                              (fn [{:keys [value label]}]
                                                                  (d/option {:value value
                                                                             :key   value}
                                                                            label))
                                                              options))))
                                      optgroups)
                                    (map
                                      (fn [{:keys [value label]}]
                                          (d/option {:value value
                                                     :key   value}
                                                    label))
                                      options)))))))
(def SelectInput (with-keechma SelectInputRenderer))

(defmulti input (fn [props] (:input/type props)))
(defmethod input :text      [props] ($ TextInput {& props}))
(defmethod input :textPlain [props] ($ PlainTextInput {& props}))
(defmethod input :date [props] ($ DateInput {& props}))
(defmethod input :password  [props] ($ PasswordInput {& props}))
(defmethod input :search    [props] ($ TextInput {& props}))
(defmethod input :textarea  [props] ($ TextArea {& props}))
(defmethod input :checkbox  [props] ($ Checkbox {& props}))
(defmethod input :select    [props] ($ SelectInput {& props}))

(defmulti wrapped-input (fn [props] (:input/type props)))
(defmethod wrapped-input :default [props] (input props))

(defmethod wrapped-input :text [props]
           (d/fieldset {:class (str "margin-auto h-16 w-full input-field-focus" (:assoc/class props))}
                       (input (assoc props :class (str (if (not (:prepend/icon props))
                                                         "min-w-full pb-3 pt-3 pl-2 outline-none bg-transparent border-b border-solid border-orange-500
                                                          focus:border-orange-200 hover:bg-gray-700 focus:bg-gray-900"
                                                         "min-w-full pb-3 pt-3 pl-8 outline-none bg-transparent border-b border-red-600 focus:border-2")
                                                       (when (:errors props) "border-redDark"))))
                       ($ Errors {& props})))

(defmethod wrapped-input :textPlain [props]
           (d/fieldset {:class (str "margin-auto h-16 w-full input-field-focus" (:assoc/class props))}
                       (input (assoc props :class (str (if (not (:prepend/icon props))
                                                         "min-w-full pb-3 pt-3 pl-2 outline-none bg-transparent
                                                          focus:border-orange-200 hover:bg-gray-700 focus:bg-gray-900"
                                                         "min-w-full pb-3 pt-3 pl-8 outline-none bg-transparent border-b border-red-600 focus:border-2")
                                                       (when (:errors props) "border-redDark"))))
                       ($ Errors {& props})))

(defmethod wrapped-input :password [props]
           (d/fieldset {:class (str "margin-auto h-16 w-full input-field-focus transparent " (:assoc/class props))}
                       (input (assoc props :class (str (if (not (:prepend/icon props))
                                                         "min-w-full pb-3 pl-2 outline-none bg-transparent border-b-3 border-gray focus:border-blueLight "
                                                         "min-w-full pb-3 pl-8 outline-none bg-transparent border-b-3 border-gray focus:border-blueLight ")
                                                       (when (:errors props) "border-redDark"))))
                       ($ Errors {& props})))

(defmethod wrapped-input :search [props]
           (d/fieldset {:class "margin-auto h-full w-full input-field-focus transparent"}
                       (input (assoc props :class  (if (not (:prepend/icon props))
                                                     "min-w-full pb-3 pl-2 outline-none bg-transparent border-b-3 border-gray focus:border-blueLight"
                                                     "min-w-full pb-3 pl-8 outline-none bg-transparent border-b-3 border-gray focus:border-blueLight")))))

(defmethod wrapped-input :textarea [props]
           (d/fieldset {:class "form-group"}
                       (input (assoc props :class "form-control form-control-lg"))
                       ($ Errors {& props})))

(defmethod wrapped-input :checkbox [props]
           (d/fieldset {:class "margin-auto h-16 w-full input-field-focus transparent"}
                       (input (assoc props :class "hidden w-6 rounded-md  h-6 pb-3"))
                       ($ Errors {& props})))

(defmethod wrapped-input :date [props]
           (d/fieldset {:class "margin-auto h-16 w-full input-field-focus transparent"}
                       (input (assoc props :class "min-w-full pb-3 pt-3 pl-2 outline-none bg-transparent border-b border-solid
                                                   border-orange-400 text-white  hover:bg-gray-700"))
                       ($ Errors {& props})))

(defmethod wrapped-input :select [props]
           (d/fieldset {:class "form-group w-full px-2 text-md"}
                       (input (assoc props :class "relative appearance-none bg-transparent
                                                   leading-tight focus:outline-none text-gray2

                                                   min-w-full pb-3 pt-3 pl-2 outline-none border-b border-solid border-orange-400
                                                   focus:border-orange-200 hover:bg-gray-700 focus:bg-gray-900

                                                   "))
                       ($ Errors {& props})))
