(ns rec.bs-multiselect.style)

(def styles
  [:.bs-multiselect-container
   [:&.active [:.zmdi {:color :tomato}]]
   [:.dropdown-container
    {:display :inline-block
     :margin 0
     :width :100%}
    [:.propositions {:border 0
                     :width :100%
                     :background :#FAFAFA}
     [:.proposition {:background :transparent}
      [:&.highlighted {:background :lightskyblue
                       :color :white}]
      [:&:hover {:background :lightgrey
                 :color :white}]]]
    [:input
     {:background :transparent
      :padding 0}
     [:&:focus {:padding-left :8px
                :border {:top 0 :left 0 :right 0}}]]]
   [:.selected-container
    {:position :relative}
    [:.selected-item
     {:background :lightgrey
      :border-radius :5px
      :display :inline-block
      :padding "6px 14px"
      :font-size :16px
      :margin "8px 8px 0 0"}
     [:span.cat {:margin-right :4px :color :white}]

     [:&:hover
      {:background :tomato
       :padding "6px 8px"}
      [:i {:display :inline}]]
     [:i {:display :none
          :font-size :14px
          :vertical-align :middle
          :margin-right "4px"}]]]])