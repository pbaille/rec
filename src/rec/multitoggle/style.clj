(ns rec.multitoggle.style)

(def styles
  [:.multitoggle
   [:.icon {:background :#eee}]
   [:.multitoggle-item
    {:display :inline-block}
    [:&.delete-button {:padding "6px 8px"
                       :border-left 0}
     [:&:hover {:color :tomato
                :background-color :transparent
                :border-color :#ccc}]]
    [:&.title
     {:border-left 0
      :padding-left 0}
     [:&:hover {:background-color :transparent
                :border-color :#ccc}]]
    [:&.selected {:background :lightskyblue
                  :color :white}]]])