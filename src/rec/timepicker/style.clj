(ns rec.timepicker.style)

(def styles
  [:body
   [:.timepicker {:-moz-appearance :textfield
                  :outline :none
                  :border :none}]
   [".timepicker::-webkit-outer-spin-button"
    ".timepicker::-webkit-inner-spin-button"
    {:-webkit-appearance :none}]])