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
    [:.delete-button
     {                                                      ;:height "34px"
      :text-align :center
      :vertical-align :middle}]]])
