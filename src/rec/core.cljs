(ns rec.core
  (:require [reagent.core :as r :refer [atom]]
            [rec.dropdown.comp :refer [dropdown1 dropdown2]]
            [rec.multiselect.comp :refer [multiselect]]
            [rec.ranges.comp :as ranges]
            [rec.rangeslider.comp :refer [rangeslider multi-rangeslider]]))

(defn app []
  [:div
   dropdown1
   dropdown2

   #_[multiselect
    {:data ["one" "two" "three" "four" "five" "six"]}]

   #_[ranges/multirange
    {:on-change (fn [x] (println x)) :min 0 :max 10}]

   #_[multi-rangeslider {:range [0 1]
                       :size 400
                       :plot-size 4
                       :height 20
                       :on-change #(println "vals: " %)}]])

(r/render-component [app]
                    (.getElementById js/document "app"))


