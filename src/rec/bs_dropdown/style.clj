(ns rec.bs-dropdown.style
  (:require [garden.units :refer [px]]))

(def left-pad 8)
(def input-height :35px)

(def styles
  [:div.bs-dropdown-container
   {:position :relative}
   [:input
    {:width :100%
     :height input-height
     :font-size :14px
     :padding-left (px left-pad)}

    #_[:&:focus
     {:outline :none
      :border-bottom :none}]]
   [:.propositions
    {:position :absolute
     :top input-height
     :left 0
     :right 0
     :z-index 10000
     :border-radius :5px
     :border "1px solid lightgrey"}
    [:.category
     {:width :100%}
     [:.cat-title {:color :lightgrey
                   :background :white
                   :font-size :18px
                   :line-height :35px
                   :padding-left (px left-pad)
                   :border-bottom "2px solid lightgrey"}]
     [:&.unique
      [:.proposition {:padding-left (px left-pad)}]]
     [:.proposition
      {:width :100%
       :padding (px 2)
       :padding-left (px (* 2 left-pad))
       :color :grey
       :text-align :left
       :background :white
       :line-height :30px
       :height (px 30)
       :vertical-align :middle}
      [:&:hover :&.highlighted {:background :lightgrey}]
      [:&.highlighted {:background :lightskyblue}]]]]])
