(ns rec.example
  (:require [reagent.core :as r :refer [atom]]
            [rec.core :as rec]))

(defn items [n]
  (mapv #(str "item" %) (range n)))

(def sample-data
  {:cat1 (items 3)
   :cat2 (items 5)
   :cat3 (items 10)})

(defn label [x]
  [:div
   {:style {:color :grey
            :padding :20px
            :font-size :18px}}
   x])

(defn app []
  [:div
   [label "simple dropdown"]
   [rec/dropdown {:data sample-data}]

   [label "categorized dropdown"]
   [rec/dropdown {:data (items 10)}]

   [label "simple multiselect"]
   [rec/multiselect
    {:data ["one" "two" "three" "four" "five" "six"]}]

   [label "categorized multiselect"]
   [rec/multiselect
    {:data {:numbers ["one" "two" "three" "four" "five" "six"]
            :animals ["dog" "cat" "cow" "rabbit"]}}]

   [label "multirange"]
   [rec/multirange
    {:on-change (fn [x] (println x)) :min 0 :max 10}]

   [label "multi-rangeslider"]
   [rec/multi-rangeslider {:range [0 1]
                       :size 400
                       :plot-size 4
                       :height 20
                       :on-change #(println "vals: " %)}]

   [label "daterange"]
   [rec/daterange
    {:on-change (fn [x] (println x))
     :from "20160101131313"
     :to "20160123123456"}]

   [rec/time-period {:on-change #(println %)}]

   [rec/multitoggle {:xs ["foo" "bar" "baz"] :max-selected 2 :on-change #(println %)}]])

(r/render-component [app]
                    (.getElementById js/document "app"))


