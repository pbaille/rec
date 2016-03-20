(ns rec.comp-selector.comp
  (:require [reagent.core :refer [atom] :as r]))

(defn comp-selector [{:keys [icon-class comps on-change placeholder]
                      :or {placeholder "Select a component"}}]
  (let [state (atom {:comps comps
                     :selected #{}})
        selected? #((:selected @state) (key %))]
    (fn []
      [:div.comp-selector

       [:div.input-group
        {:z-index :auto}
        [:div.input-group-addon
         [:i.fa {:class icon-class}]]
        [:div.dropdown.form-control
         {:style {:cursor :pointer
                  :z-index :auto}}
         [:div {:data-toggle "dropdown"}
          placeholder]
         [:ul.dropdown-menu
          {:style {:z-index 10}}
          (doall
            (for [[name* _] (filter (comp not selected?) (:comps @state))]
              ^{:key (gensym)}
              [:li {:style {:height "30px"
                            :line-height :30px
                            :padding-left :15px}
                    :on-click #(swap! state update :selected conj name*)}
               (name name*)]))]]]
       [:div.selected-comps
        {:style {:padding :20px}}
        (doall
          (for [[name comp] (filter selected? (:comps @state))]
            (assoc-in comp [1 :on-delete] #(swap! state update :selected disj name))))]])))
