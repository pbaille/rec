(ns rec.multirangeslider.style)

(def styles
  [:.multirangeslider-container
   [:.icon {:background :#eee}]
   [:.add-button {:border-right 0}
    [:&:hover {:background-color :transparent
               :border-color :#ccc
               :border-right 0}]
    [:&:active {:box-shadow :none}]]
   [:.delete-button {:padding "6px 8px"
                      :border-left 0}
    [:&:hover {:color :tomato
               :background-color :transparent
               :border-color :#ccc
               :border-left 0}]]
   [:.rangeslider-container
    {:padding-top :3px}
    [:.delete-btn {:border-right 0}
     [:&:hover {:background-color :transparent
                :border-color :#ccc
                :color :tomato}]]
    [:.center-zone
     {:border {:right 0 :left 0}
      :padding {:right :1px
                :left :1px}}
     [:&:hover {:background-color :transparent
                :border-color :#ccc}]
     [:&:active {:box-shadow :none}]
     [:div.input-wrap {:height :20px}
      [:input {:padding {:top :6px
                         :bottom :5px}}]]]
    [:.action-btn {:border-left 0}
     [:&:hover {:background-color :transparent
                :border-color :#ccc
                :color :tomato}]]]])