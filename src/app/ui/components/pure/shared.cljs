(ns app.ui.components.pure.shared
  (:require [helix.dom :as d]
            [helix.hooks :as hooks]
            [helix.core :as hx :refer [<>]]
            [keechma.next.helix.lib :refer [defnc]]))

(defnc AddNewItem [_]
       (d/div {:class "flex justify-end py-8 px-8 absolute bottom-0 w-full"}
              (d/button {:class "rounded-full bg-orange-600 text-white h-20 w-20 justify-center items-center text-4xl font-thin"} "+")))
