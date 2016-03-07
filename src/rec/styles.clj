(ns rec.styles
  (:require [garden.def :refer [defstylesheet defstyles]]
            [garden.units :refer [px]]

            [rec.dropdown.style :as ac]
            [rec.multiselect.style :as ms]
            [rec.rangeslider.style :as rs]
            [rec.ranges.style :as r]))

(def border-box
  [:html
   {:box-sizing :border-box}
   [:* :*:before :*:after
    {:box-sizing :inherit}]])

(defstyles styles
           ac/styles
           ms/styles
           r/styles
           rs/styles
           border-box
           #_[:#app
              [:.dropdown-container
               {:width :40%
                :display :inline-block}]
              [:.multiselect-container
               {:width :40%
                :display :inline-block}]])
