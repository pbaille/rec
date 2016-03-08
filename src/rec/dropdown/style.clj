(ns rec.dropdown.style
  (:require [garden.units :refer [px]]))

(def left-pad 8)
(def input-height :35px)

(def styles
  [:div.dropdown-container
   {:margin (px 10)
    :position :relative}
   [:input
    {:width :100%
     :border :none
     :height input-height
     :font-size :14px
     :background-color :#FAFAFA
     :padding-left (px left-pad)}

    [:&:focus
     {:outline :none
      :border-bottom :none}]]
   [:.propositions
    {:position :absolute
     :top input-height
     :left 0
     :right 0
     :z-index 10000}
    [:.category
     [:.cat-title {:color :lightgrey
                   :background :white
                   :font-size :18px
                   :line-height :30px
                   :padding-left (px left-pad)
                   :border-bottom "2px solid lightgrey"}]
     [:&.unique
      [:.proposition {:padding-left (px left-pad)}]]
     [:.proposition
      {:padding (px 2)
       :padding-left (px (* 2 left-pad))
       :color :grey
       :background :white
       :height (px 25)
       :vertical-align :middle}
      [:&:hover :&.highlighted {:background :lightgrey}]
      [:&.highlighted {:background :lightskyblue}]]]]])
