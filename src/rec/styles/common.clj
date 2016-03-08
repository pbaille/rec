(ns rec.styles.common)

(def border-box
  [:html
   {:box-sizing :border-box}
   [:* :*:before :*:after
    {:box-sizing :inherit}]])

(def fonticons
  [:i
   {:font-size "20px !important"
    :vertical-align :middle
    :text-align :center
    :width :35px}
   [:&:hover
    {:color :tomato}]])

(def unselectable
  {:-webkit-user-select :none
   :-moz-user-select :none
   :-ms-user-select :none})
