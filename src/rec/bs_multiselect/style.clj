(ns rec.bs-multiselect.style)

(def styles
  [:.bs-multiselect-container
   [:&.active [:.zmdi {:color :tomato}]]
   [:.delete-button {:background-color :transparent
                     :padding "6px 8px"
                     :border-left 0}
    [:&:hover {:color :tomato
               :border-color :#ccc
               :border-left 0}]]
   [:.bs-dropdown-container {:border-right 0
                             :box-shadow :none}]
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