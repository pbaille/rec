(ns rec.styles
  (:require [garden.def :refer [defstylesheet defstyles]]
            [garden.units :refer [px]]

            [rec.dropdown.style :as ac]
            [rec.multiselect.style :as ms]
            [rec.rangeslider.style :as rs]
            [rec.ranges.style :as r]
            [rec.styles.common :refer [border-box]]
            [rec.timepicker.style :as tp]
            [rec.daterange.style :as dr]))

(defstyles styles
           ac/styles
           ms/styles
           r/styles
           rs/styles
           tp/styles
           dr/styles
           border-box)
