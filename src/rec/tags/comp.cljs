(ns rec.tags.comp
  (:require [reagent.core :as r :refer [atom]]
            [rec.dropdown.comp :refer [dropdown]]))

(defn tags [{:keys [on-change style data]}]
  (let [state (atom {:selected-type nil
                     :options ["Queries" "Tags" "Tag-groups"]
                     :data data
                     :value-focus false
                     :values #{}})

        values-idx (cljs.core/atom 0)

        current-choices #(if (:selected-type @state)
                          (clojure.set/difference
                            (set (get-in @state [:data (keyword (clojure.string/lower-case (:selected-type @state)))]))
                            (set (map :value (:values @state))))
                          (into {}
                                (map (fn [[k v]]
                                       [k (clojure.set/difference
                                            (set v)
                                            (set (map :value (:values @state))))])
                                     (:data @state))))
        add-to-values #(reset! state
                               (-> @state
                                   (update :values conj
                                           (with-meta {:type (or (and %2 (name %2)) (:selected-type @state))
                                                       :value %}
                                                      {:idx (inc @values-idx)}))
                                   (assoc :selected-type nil
                                          :value-focus false)))

        dd-id (str (gensym))

        singularize-type {"queries" "Query"
                          "Queries" "Query"
                          "tags" "Tag"
                          "Tags" "Tag"
                          "tag-groups" "Tag-group"
                          "Tag-groups" "Tag-group"}]

    (fn []
      [:div.tags-container
       {:style style}
       [:div.input-group
        [:div.input-group-addon [:span.fa.fa-tags]]
        [:div.dropdown.input-group-addon
         [:span
          {:data-toggle "dropdown"
           :id dd-id}
          (or (:selected-type @state) "Type")
          "  "
          [:span.caret {:style {:margin-left :15px}}]]
         [:ul.dropdown-menu
          {:aria-labelledby dd-id}
          (for [o (disj (set (:options @state)) (:selected-type @state))]
            ^{:key (gensym)}
            [:li {:on-click #(swap! state assoc :selected-type o :value-focus true)}
             o])]]
        [(dropdown
           {:data (current-choices)
            :placeholder "Select something"
            :focus (:value-focus @state)
            :on-select #(add-to-values (:name %2) (:category %2))})]]
       [:div.values
        {:style {:padding-left :20px}}
        (for [{:keys [type value] :as v} (sort-by (comp :idx meta) (:values @state))]
          ^{:key (gensym)}
          [:div
           {:style {:margin-top :20px}}
           [:div.value.btn-group
            [:div.btn.btn-default
             {:on-click #(swap! state update :values disj v)}
             [:i.delete-button.fa.fa-times]]
            [:span.btn.btn-default.value-type (singularize-type type)]
            [:span.btn.btn-default.value-txt value]]])]])))
