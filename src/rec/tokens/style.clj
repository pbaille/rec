(ns rec.tokens.style)

(def styles
  [:.tokens-container
   [:.values
    [:.value
     [:.value-type {:border-left 0
                    :padding-left 0}]
     [:.value-type :.value-txt
      [:&:hover {:background-color :transparent
                 :border-color :#ccc}]]
     [:.delete-button
      {:border-right 0}
      [:&:hover
       {:color :tomato
        :background-color :transparent
        :border-color :#ccc}]]
     [:.value-edit
      {:border-left 0}
      [:&:hover
       {:color :tomato
        :background-color :transparent
        :border-color :#ccc}]]]]])
