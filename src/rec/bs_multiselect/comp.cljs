(ns rec.bs-multiselect.comp
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [reagent.core :as r :refer [atom]]
            [rec.bs-dropdown.comp :refer [bs-dropdown format-data find-by-id]]))

(defn- set-selection! [state item selected?]
  (swap! state
         assoc
         :data
         (map #(if (= item %)
                (assoc % :selected selected?)
                %)
              (:data @state))))

(defn bs-multiselect
  [{:keys [data selected placeholder on-change icon-class]
    :or {selected []
         data []
         placeholder "Type something"
         on-change identity
         icon-class :zmdi-github-alt}}]
  (let [formatted (map #(assoc % :selected false) (format-data data))
        state (atom {:data formatted
                     :focus false})
        selected (reaction (filter :selected (:data @state)))]
    (fn []
      [:div.bs-multiselect-container
       {:class (when (:focus @state) "active")}
       [(bs-dropdown
          {:data (filter (comp not :selected) (:data @state))
           :placeholder placeholder
           :value ""
           :on-focus #(swap! state assoc :focus true)
           :on-blur #(swap! state assoc :focus false)
           :focus (:focus @state)
           :on-select (fn [_ item]
                        (set-selection! state item true)
                        (on-change @selected))})]
       [:div.selected-container
        (for [item @selected]
          ^{:key (gensym)}
          [:div.selected-item
           {:on-mouse-down (fn []
                             (set-selection! state item false)
                             (on-change @selected))}
           [:i.zmdi.zmdi-delete]
           (when-let [cat (:category item)] [:span.cat (name cat)])
           [:span.name (:name item)]])]])))
