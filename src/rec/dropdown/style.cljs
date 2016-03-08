(ns rec.dropdown.style
  (:require [garden.core :refer [css]]
            [garden.units :refer [px]]
            [goog.style]))

(def left-pad 8)
(def input-height :35px)

(defn styles [scope-selector overides]
  (goog.style/installStyles
    (css [scope-selector
          [:&.dropdown-container
           (merge
             {:margin (px 10)
              :position :relative}
             (:.dropdown-container overides))
           [:input
            (merge
              {:width :100%
               :border :none
               :height input-height
               :font-size :14px
               :background :#FAFAFA
               :padding-left (px left-pad)}
              (:input overides))

            [:&:focus
             (merge
               {:outline :none
                :border-bottom :none}
               (get-in overides [:input :focus]))]]
           [:.propositions
            {:position :absolute
             :top input-height
             :left 0
             :right 0
             :z-index 1000}
            [:.category
             [:.cat-title (merge
                            {:color :lightgrey
                             :background :white
                             :font-size :18px
                             :line-height :30px
                             :padding-left (px left-pad)
                             :border-bottom "2px solid lightgrey"}
                            (:.cat-title overides))]
             [:&.invisible
              [:.proposition {:padding-left (px left-pad)}]]
             [:.proposition
              (merge
                {:padding (px 2)
                 :padding-left (px (* 2 left-pad))
                 :color :grey
                 :background :white
                 :height (px 25)
                 :vertical-align :middle}
                (:.proposition overides))
              [:&:hover :&.highlighted (merge {:background :lightgrey}
                                              (get-in overides [:.proposition :hover]))]
              [:&.highlighted (merge {:background :lightskyblue}
                                     (get-in overides [:.proposition :.highlighted]))]]]]]])))


