(ns rec.sidebar.comp
  (:require [rec.multiselect.comp :refer [multiselect]]
            [rec.rangeslider.comp :refer [rangeslider multi-rangeslider]]
            [rec.timepicker.comp :refer [timepicker]]))

(def conf
  {:project-focus
   {:queries [:query1
              :query2
              :query3
              :query4]
    :tags [:tag1
           :tag2
           :tag3
           :tag4]
    :tag-groups [:tag-group1
                 :tag-group2
                 :tag-group3
                 :tag-group4]}
   :authors
   {:gender [:male :female]
    :age []
    :location []
    :klout []}

   :text-content
   {:shared [true false]
    :sentiment [:positive :negative :neutral]
    :topics [:politics :sports :finance :entertainment
             :technology :movies-and-tv-series
             :hotels-and-restaurants :agriculture-and-environment
             :luxury-and-fashion :food-and-beverages :automobile :health
             :transport :tourism :energy :others]
    :brands []}

   :media-content
   {:media-type [:urls :videos :images]
    :topics []
    :ages []
    :gender [:male :female]}})
