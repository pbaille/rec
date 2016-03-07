(ns rec.core
  (:require [reagent.core :as r :refer [atom]]
            [rec.dropdown.comp :refer [dropdown]]
            [rec.multiselect.comp :refer [multiselect]]
            [rec.ranges.comp :as ranges]))

(defn app []
  [:div
   [dropdown
    {:data ["one" "two" "three" "four" "five" "six"]}]
   [multiselect
    {:data ["one" "two" "three" "four" "five" "six"]}]
   [ranges/multirange
    {:on-change (fn [x] (println x)) :min 0 :max 10}]])

(r/render-component [app]
                    (.getElementById js/document "app"))


