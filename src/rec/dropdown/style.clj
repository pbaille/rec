(ns rec.dropdown.style)

(def left-pad "8px")
(def input-height :35px)

(def styles
  [:div.dropdown-container
   {:width :100%
    :margin :10px
    :position :relative}
   [:input
    {:width :100%
     :border :none
     :height input-height
     :font-size :14px
     :background-color :#FAFAFA
     :padding-left left-pad}

    [:&:focus
     {:outline :none
      :border-bottom :none}]]
   [:.propositions
    {:position :absolute
     :top input-height
     :left 0
     :right 0
     :z-index 1000}
    [:.proposition
     {:padding "2px"
      :padding-left left-pad
      :color :grey
      :background :white
      :height :25px
      :vertical-align :middle}
     [:&:hover :&.highlighted {:background :lightgrey}]
     [:&.highlighted {:background :lightskyblue}]]]])
