(ns rec.multiselect.comp
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [reagent.core :as r :refer [atom]]
            [rec.dropdown.comp :refer [dropdown format-data find-by-id]]))

(defn- set-selection! [state item selected?]
  (swap! state
         assoc
         :data
         (map #(if (= item %)
                (assoc % :selected selected?)
                %)
              (:data @state))))

(defn multiselect
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
      [:div.multiselect-container
       {:class (when (:focus @state) "active")}
       ;; :o
       [:div.select-bar
        [:i {:class (str "zmdi " (name icon-class))}]
        [(dropdown
           {:data (filter (comp not :selected) (:data @state))
            :placeholder placeholder
            :value ""
            :on-focus #(swap! state assoc :focus true)
            :on-blur #(swap! state assoc :focus false)
            :focus (:focus @state)
            :on-select (fn [_ item]
                         (set-selection! state item true)
                         (on-change @selected))})]]
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
