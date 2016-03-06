(ns rec.multi-select.style)

(def styles
  [:.multiselect-container
   {:padding :10px}
   [:.select-bar
    {:background :#FAFAFA}
    [:.zmdi {:font-size :22px
             :width :22px
             :padding-left :6px
             :margin :auto
             :vertical-align :middle}]
    [:.autocomplete-container
     {:display :inline-block
      :margin 0
      :width "calc(100% - 22px)"}
     [:.propositions {:border 0
                      :width :100%
                      :background :#FAFAFA}
      [:.proposition {:background :transparent}
       [:&.highlighted {:background :lightskyblue}]
       [:&:hover {:background :lightgrey
                  :color :white}]]]
     [:input
      {:background :transparent}
      [:&:focus {:border {:top 0 :left 0 :right 0}}]]]]
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
          :margin-right "4px"}]]]
   ["::-webkit-scrollbar"
    {:width :5px}]
   ["::-webkit-scrollbar-thumb"
    {:border-radius :5px
     :background :grey}]])