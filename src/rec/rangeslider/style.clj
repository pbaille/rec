(ns rec.rangeslider.style
 (:require [rec.styles.common :refer [fonticons unselectable]]))

(def styles
  [:body
   [:.rangeslider-container
    {:padding :20px
     :padding-bottom :10px
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
    [:.add-rangeslider
     {:background :#FAFAFA
      :line-height :35px
      :margin :10px
      :margin-bottom :20px}
     fonticons
     [:span {:font-size :18px}]]]])
