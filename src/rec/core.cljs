(ns rec.core
  (:require [reagent.core :as r :refer [atom]]
            [rec.autocomplete.comp :refer [autocomplete]]
            [rec.multi-select.comp :refer [multi-select]]
            [rec.ranges.comp :as ranges]))

(defn app []
  [:div
   [autocomplete
    {:data ["one" "two" "three" "four" "five" "six"]}]
   [multi-select
    {:data ["one" "two" "three" "four" "five" "six"]}]
   [ranges/multirange
    {:on-change (fn [x] (println x)) :min 0 :max 10}]])

(r/render-component [app]
                    (.getElementById js/document "app"))


