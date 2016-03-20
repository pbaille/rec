(ns rec.styles
  (:require [garden.def :refer [defstylesheet defstyles]]
            [garden.units :refer [px]]

            [rec.dropdown.style :as ac]
            [rec.multiselect.style :as ms]
            [rec.styles.common :refer [border-box]]
            [rec.timepicker.style :as tp]
            [rec.daterange.style :as dr]
            [rec.multitoggle.style :as mt]
            [rec.tokens.style :as t]
            [rec.tags.style :as tags]
            [rec.multirangeslider.style :as mrs]
            [rec.locationpicker.style :as lp]))

(defstyles styles
           ac/styles
           ms/styles
           tp/styles
           dr/styles
           mt/styles
           t/styles
           tags/styles
           mrs/styles
           lp/styles
           border-box)
