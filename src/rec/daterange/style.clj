(ns rec.daterange.style)

(def styles
  [:body
   [:.daterange
    {:display :flex
     :flex-flow "row nowrap"
     :justify-content :flex-start
     :padding :20px}
    [:.datefrom :.dateto
     {:display :inline-block
      :width :210px}
     [:td.active {:background :lightskyblue}]
     [:th {:border :0px
           :height :30px}]]
    [:.presets {:display :flex
                :width :100px
                :flex-flow "column nowrap"
                :justify-content "center"
                :margin-right :10px }
     [:.preset {:text-align :center
                :background :#F7F7F7
                :border-radius :2px
                :margin :2px
                :padding :4px
                :color :#777
                :font-size :14px}
      [:&:hover {:background :lightskyblue
                 :color :white}]]]
    [:.timepicker {:color :#777
                   :border-radius :2px
                   :display :block
                   :margin :auto
                   :height :30px
                   :font-size :14px
                   :padding-left :18px
                   :background :#F7F7F7
                   :cursor :pointer}]]])