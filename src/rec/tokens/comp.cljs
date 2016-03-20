(ns rec.tokens.comp
  (:require [reagent.core :as r :refer [atom]]
            [rec.dropdown.comp :refer [dropdown]]))

(def width "100px")

(defn tokens [{:keys [on-change style]}]
  (let [state (atom {:selected nil
                     :options ["Text" "Author" "Hashtag"]
                     :input-focus false
                     :values #{}})
        dd-id (str (gensym))
        values-idx (cljs.core/atom 0)
        input-el #(js/$ "input" ".tokens-container")
        focus #(.focus (input-el))
        blur #(.blur (input-el))
        set-val #(.val (input-el) %)
        get-val #(.val (input-el))
        clear #(.val (input-el) "")
        select-text #(.select (input-el))

        add-value-and-clear #(reset! state
                                    (-> @state
                                        (update :values conj (with-meta {:type (:selected @state) :value %}
                                                                        {:idx (inc @values-idx)}))
                                        (assoc :selected nil)))]
    (fn []
      [:div.tokens-container
       {:style style}
       [:div.input-group
        [:div.input-group-addon [:span.fa.fa-search]]
        [:div.dropdown.input-group-addon
         [:span
          {:id dd-id
           :data-toggle "dropdown"}
          (or (:selected @state) "Type")
          "  "
          [:span.caret {:style {:margin-left :15px}}]]
         [:ul.dropdown-menu
          {:aria-labelledby dd-id}
          (for [o (disj (set (:options @state)) (:selected @state))]
            ^{:key (gensym)}
            [:li {:on-click #(do (swap! state assoc :selected o :input-focus true)
                                 (focus))} o])]]
        [:input.form-control
         {:type "text"
          :on-key-up (fn [e] (when (and (= (.. e -nativeEvent -keyCode) 13)
                                        (seq (.. e -target -value)))
                               (if (:selected @state)
                                 (do (add-value-and-clear (.. e -target -value))
                                     (blur)
                                     (clear)
                                     (on-change (:values @state)))
                                 (.click (js/$ (str "#" dd-id))))))
          :placeholder "Enter value"}]
        [:span.input-group-addon
         {:on-click (fn []
                      (let [v (get-val)]
                        (when (seq v)
                          (add-value-and-clear v)
                          (clear)
                          (on-change (:values @state)))))}
         [:i.fa.fa-check]]]
       [:div.values
        {:style {:padding-left :20px}}
        (doall
          (for [{:keys [type value] :as v} (sort-by (comp :idx meta) (:values @state))]

            (let [edit #(do (reset! state
                                    (-> @state
                                        (assoc :selected type)
                                        (update :values disj v)))
                            (set-val value)
                            (focus)
                            (select-text))]
              ^{:key (gensym)}
              [:div
               {:style {:margin-top :20px}}
               [:div.value.btn-group
                [:div.delete-button.btn.btn-default
                 {:on-click #(swap! state update :values disj v)}
                 [:i.fa.fa-times]]
                [:span.btn.btn-default.value-type type]
                [:span.btn.btn-default.value-txt value]
                [:span.btn.btn-default.value-edit
                 {:on-click edit}
                 [:span.fa.fa-pencil]]]])))]])))



