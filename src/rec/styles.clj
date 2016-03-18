(ns rec.styles
  (:require [garden.def :refer [defstylesheet defstyles]]
            [garden.units :refer [px]]

            [rec.dropdown.style :as ac]
            [rec.bs-dropdown.style :as bsdd]
            [rec.multiselect.style :as ms]
            [rec.bs-multiselect.style :as bsms]
            [rec.rangeslider.style :as rs]
            [rec.ranges.style :as r]
            [rec.styles.common :refer [border-box]]
            [rec.timepicker.style :as tp]
            [rec.daterange.style :as dr]
            [rec.multitoggle.style :as mt]
            [rec.tokens.style :as t]
            [rec.multirangeslider.style :as mrs]
            [rec.locationpicker.style :as lp]))

(defstyles styles
           ac/styles
           bsdd/styles
           ms/styles
           bsms/styles
           r/styles
           rs/styles
           tp/styles
           dr/styles
           mt/styles
           t/styles
           mrs/styles
           lp/styles
           border-box)
