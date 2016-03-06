(ns rec.autocomplete.style)

(def borders {:border "1px solid lightgrey"})
(def left-pad "8px")
(def input-height :35px)

(def styles
  [:div.autocomplete-container
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
     borders
     {:outline :none
      :border-bottom :none}]]
   [:.propositions
    borders
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
