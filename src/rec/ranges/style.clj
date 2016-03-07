(ns rec.ranges.style)

(def styles
  [:body
   [:.simplerange :.range
    {:display :inline-block
     :background :lightgrey
     :border-radius :5px
     :padding :4px
     :padding-left :10px}
    [:input {:margin-left :8px
             :border-radius :4px
             :border 0
             :background :white
             :font-size :14px}
     [:&:focus
      {:outline :none}]]]
   [:.multirange-container
    {:padding :10px}
    [:.add :.constructors
     {:width :100%
      :border :none
      :line-height :35px
      :font-size :18px
      :color :grey
      :background :#FAFAFA}
     [:i
      {:vertical-align :middle
       :font-size :20px
       :margin "0 4px"}
      [:&:hover
       {:color :tomato}]]
     [:span.placeholder
      {:padding :6px
             :font-size :18px}]]
    [:.constructor
     {:background :lightskyblue
      :margin "0 8px 0 0"
      :padding "3px 8px"
      :border-radius :5px
      :color :white}]
    [:.ranges
     [:.ranges-item
      {:display :inline-block
       :margin "10px 10px 0 0"}
      [:i {:display :none}]
      [:&:hover
       [:i {:display :inline-block
            :color :tomato
            :font-size :20px
            :vertical-align :middle
            :margin "0 4px"}]]]]]])