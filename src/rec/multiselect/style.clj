(ns rec.multiselect.style)

(def styles
  [:.multiselect-container
   {:padding :10px}
   [:&.active [:.zmdi {:color :tomato}]]
   [:.select-bar
    {:background :#FAFAFA}
    [:.zmdi {:font-size :22px
             :width :36px
             :padding "0 10px"
             :margin :auto
             :vertical-align :middle}]
    [:.dropdown-container
     {:display :inline-block
      :margin 0
      :width "calc(100% - 36px)"}
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
                 :border {:top 0 :left 0 :right 0}}]]]]
   [:.selected-container
    {:position :relative}
    [:.selected-item
     {:background :lightgrey
      :border-radius :5px
      :display :inline-block
      :padding "6px 14px"
      :font-size :16px
      :margin "8px 8px 0 0"}
     [:&:hover
      {:background :tomato
       :padding "6px 8px"}
      [:i {:display :inline}]]
     [:i {:display :none
          :font-size :14px
          :margin-right "4px"}]]]])