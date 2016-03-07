(ns rec.rangeslider.style)

(def unselectable
  {:-webkit-user-select :none
   :-moz-user-select :none
   :-ms-user-select :none})

(def styles
  [:body
   [:.rangeslider-container
    {:padding :20px
     :display :inline-block}
    unselectable
    [:&.active {:cursor :ew-resize}]
    [:.rangeslider-track
     {:box-sizing :border-box
      :background :#FAFAFA}
     unselectable
     [:.plot
      {:position :absolute
       :cursor :ew-resize
       :background :lightgrey
       :display :inline-block}
      unselectable
      [:.plot-label
       {:display :inline-block
        :position :relative
        :bottom :20px
        :width :100px
        :text-align :center
        :right :50px}
       unselectable]]
     [:.inside
      {:position :absolute
       :display :inline-block
       :background :lightskyblue}
      unselectable]]]
   [:.multi-rangeslider
    {:color :grey}
    [:i
     {:font-size :22px}
     [:&:hover
      {:color :tomato}]]
    [:span {:padding :10px
            :font-size :18px}]]])
