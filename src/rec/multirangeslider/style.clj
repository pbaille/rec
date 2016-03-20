(ns rec.multirangeslider.style)

(def styles
  [:.multi-bsrs-container
   [:.icon {:background :#eee}]
   [:.delete-button {:padding "6px 8px"
                      :border-left 0}
    [:&:hover {:color :tomato
               :background-color :transparent
               :border-color :#ccc
               :border-left 0}]]
   [:.bs-rangeslider-container
    {:padding-top :3px}
    [:.range-head {:padding-left :10px}]

    [:.center-zone
     [:div.input-wrap {:height :20px}
      [:input {:padding {:top :6px
                         :bottom :5px}}]]]]])