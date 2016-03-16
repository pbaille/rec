(ns rec.tokens.comp
  (:require [reagent.core :as r :refer [atom]]))

(def width "100px")

(defn tokens [{:keys [on-change style]}]
  (let [state (atom {:selected "text"
                     :options ["text" "author" "hashtag"]
                     :input-focus false
                     :values #{}
                     :hover false})
        values-idx (cljs.core/atom 0)
        input-el #(js/$ "input" ".tokens-container")
        focus #(.focus (input-el))
        blur #(.blur (input-el))
        set-val #(.val (input-el) %)
        get-val #(.val (input-el))
        clear #(.val (input-el) "")
        select-text #(.select (input-el))]
    (fn []
      [:div.tokens-container
       {:style style
        :on-mouse-enter #(swap! state assoc :hover true)
        :on-mouse-leave #(swap! state assoc :hover false)}
       [:div.input-group
        [:div.dropdown.input-group-addon
         {:style {:width "100px"
                  :cursor :pointer}}
         [:span#tokens-dd
          {:data-target "#"
           :data-toggle "dropdown"
           :aria-haspopup "true"
           :aria-expanded "false"}
          (:selected @state)]
         [:ul.dropdown-menu
          {:aria-labelledby "tokens-dd"
           :style {:min-width :100%}}
          (for [o (disj (set (:options @state)) (:selected @state))]
            ^{:key (gensym)}
            [:li {:style {:height "30px"
                          :text-align :center
                          :line-height :30px}
                  :on-click #(do (swap! state assoc :selected o :input-focus true)
                                 (focus))} o])]]
        [:input.form-control
         {:type "text"
          :on-key-up (fn [e] (when (and (= (.. e -nativeEvent -keyCode) 13)
                                        (seq (.. e -target -value)))
                               (let [v (with-meta {:type (:selected @state) :value (.. e -target -value)}
                                                  {:idx (inc @values-idx)})]
                                 (swap! state update :values conj v)
                                 (blur)
                                 (clear)
                                 (on-change (:values @state)))))}]
        [:span.input-group-addon
         {:on-click (fn []
                      (let [v (get-val)]
                        (when (seq v)
                          (swap! state update :values conj (with-meta {:type (:selected @state) :value v}
                                                                      {:idx (inc @values-idx)}))
                          (clear)
                          (on-change (:values @state)))))}
         [:i.fa.fa-check]]]
       [:div.values
        (for [{:keys [type value] :as v} (sort-by (comp :idx meta) (:values @state))]

          (let [edit #(do (reset! state
                                  (-> @state
                                      (assoc :selected type)
                                      (update :values disj v)))
                          (set-val value)
                          (focus)
                          (select-text))]
            ^{:key (gensym)}
            [:div.value.btn-group
             [:span.delete-button.fa.fa-times.btn.btn-default
              {:on-click #(swap! state update :values disj v)
               :style {:line-height :32px
                       :padding "0 10px"
                       :vertical-align :middle}}]
             [:span.btn.btn-default.value-type {:on-click edit} type]
             [:span.btn.btn-default.value-txt {:on-click edit
                                               :style (when (:hover @state) {:padding-right 1
                                                                             :border-right 0})} value]
             (when (:hover @state) [:span.btn.btn-default.value-edit
                                    {:on-click edit}
                                    [:span.fa.fa-pencil]])]))]])))

