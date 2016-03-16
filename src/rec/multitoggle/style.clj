(ns rec.multitoggle.style)

(def styles
  [:.multitoggle
   [:.multitoggle-item
    {:display :inline-block}
    [:&.delete-button {:border-right 0
                       :padding "6px 8px"}
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