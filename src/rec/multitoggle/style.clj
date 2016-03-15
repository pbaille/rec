(ns rec.multitoggle.style)

(def styles
  [:.multitoggle
   [:.multitoggle-item
    {:display :inline-block}
    [:&.selected {:background :lightskyblue
                  :color :white}]]])