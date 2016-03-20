(ns rec.tokens.style)

(def styles
  [:.tokens-container
   [:.dropdown.input-group-addon
    {:width :100px
     :cursor :pointer
     :border-right 0}
    [:.dropdown-menu
     {:min-width :100%}
     [:li {:height :30px
           :text-align :center
           :line-height :30px}]]]
   [:.values
    [:.value
     [:.value-type {:border-left 0
                    :padding-left 0}]
     [:.value-txt {:border-right 0
                    :padding-right :1px}]
     [:.value-type :.value-txt
      {:background-color :transparent}
      [:&:hover {:background-color :transparent
                 :border-color :#ccc}]]
     [:.delete-button
      {:border-right 0
       :background-color :transparent}
      [:&:hover
       {:color :tomato
        :background-color :transparent
        :border-color :#ccc}]]
     [:.value-edit
      {:border-left 0}
      [:&:hover
       {:color :tomato
        :border-left 0
        :background-color :transparent
        :border-color :#ccc}]]]]])
