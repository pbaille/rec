(ns rec.tags.style)

(def styles
  [:div.tags-container
   {}
   [:.input-group
     {:width :100%}
    [:.dropdown.input-group-addon
     {:width "100px"
      :cursor :pointer
      :border-right 0}
     [:.dropdown-menu
      {:min-width :100%}
      [:li {:height "30px"
            :text-align :center
            :line-height :30px}]]]]
   [:.values
    [:.value-type {:border-left 0
                   :padding-left 0}]
    [:.value-type :.value-txt
     [:&:hover {:background-color :transparent
                :border-color :#ccc}]]
    [:.delete-button
     {:text-align :center
      :vertical-align :middle
      :border-right 0}
     [:&:hover
      {:color :tomato
       :background-color :transparent
       :border-color :#ccc}]]]])
