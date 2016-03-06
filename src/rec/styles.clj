(ns rec.styles
  (:require [garden.def :refer [defstylesheet defstyles]]
            [garden.units :refer [px]]

            [rec.autocomplete.style :as ac]
            [rec.multi-select.style :as ms]
            [rec.scrollable-panel.style :as sp]
            [rec.ranges.style :as r]))

(def border-box
  [:html
   {:box-sizing :border-box}
   [:* :*:before :*:after
    {:box-sizing :inherit}]])

(defstyles styles
           ac/styles
           ms/styles
           sp/styles
           r/styles
           border-box
           #_[:#app
              [:.autocomplete-container
               {:width :40%
                :display :inline-block}]
              [:.multiselect-container
               {:width :40%
                :display :inline-block}]])
