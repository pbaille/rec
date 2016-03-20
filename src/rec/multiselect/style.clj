(ns rec.multiselect.style)

(def styles
  [:.multiselect-container
   [:&.active [:.zmdi {:color :tomato}]]
   [:.delete-button {:background-color :transparent
                     :padding "6px 8px"
                     :border-left 0}
    [:&:hover {:color :tomato
               :border-color :#ccc
               :border-left 0}]]
   [:.dropdown-container {:border-right 0
                             :box-shadow :none}]
   [:.selected-container
    {:position :relative}
    [:.selected-item
     {:padding "10px 10px 0 0"}
     [:.delete-btn {:border-right 0}
      [:&:hover
       {:color :tomato
        :background-color :transparent
        :border-color :#ccc}]]
     [:.name {:border-left 0
              :padding-left 0}
      [:&:hover
       {:background-color :transparent
        :border-color :#ccc}]]]]])