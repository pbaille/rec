(ns rec.core
  (:require [reagent.core :as r :refer [atom]]
            [rec.dropdown.comp :as dropdown]
            [rec.multiselect.comp :as multiselect]
            [rec.ranges.comp :as ranges]
            [rec.rangeslider.comp :as rs]
            [rec.daterange.comp :as dr]))

(def dropdown dropdown/dropdown)
(def multiselect multiselect/multiselect)
(def multirange ranges/multirange)
(def rangeslider rs/rangeslider)
(def multi-rangeslider rs/multi-rangeslider)
(def daterange dr/daterange)


