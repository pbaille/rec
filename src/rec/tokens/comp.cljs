(ns rec.tokens.comp
  (:require [reagent.core :as r :refer [atom]]
            [rec.bs-dropdown.comp :refer [bs-dropdown]]))

(def width "100px")

(defn tokens [{:keys [on-change style]}]
  (let [state (atom {:selected nil
                     :options ["Text" "Author" "Hashtag"]
                     :input-focus false
                     :values #{}})
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
       {:style style}
       [:div.input-group
        [:div.input-group-addon [:span.fa.fa-search]]
        [:div.dropdown.input-group-addon
         {:style {:width "100px" :cursor :pointer :border-right 0}}
         [:span#tokens-dd
          {:data-target "#"
           :data-toggle "dropdown"
           :aria-haspopup "true"
           :aria-expanded "false"}
          (or (:selected @state) "Type")
          "  "
          [:span.caret {:style {:margin-left :15px}}]]
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
                               (if (:selected @state)
                                 (do (reset! state
                                             (-> @state
                                                 (update :values conj (with-meta {:type (:selected @state) :value (.. e -target -value)}
                                                                                 {:idx (inc @values-idx)}))
                                                 (assoc :selected nil)))
                                     (blur)
                                     (clear)
                                     (on-change (:values @state)))
                                 (.click (js/$ "#tokens-dd")))))
          :placeholder "Enter value"}]
        [:span.input-group-addon
         {:on-click (fn []
                      (let [v (get-val)]
                        (when (seq v)
                          (reset! state
                                  (-> @state
                                      (update :values conj (with-meta {:type (:selected @state) :value v}
                                                                      {:idx (inc @values-idx)}))
                                      (assoc :selected nil)))
                          (clear)
                          (on-change (:values @state)))))
          }
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
                [:span.delete-button.fa.fa-times.btn.btn-default
                 {:on-click #(swap! state update :values disj v)
                  :style {:line-height :32px
                          :padding "0 10px"
                          :vertical-align :middle}}]
                [:span.btn.btn-default.value-type {:on-click edit} type]
                [:span.btn.btn-default.value-txt {:on-click edit
                                                  :style (when (:hover @state) {:padding-right 1
                                                                                :border-right 0})} value]
                [:span.btn.btn-default.value-edit
                 {:on-click edit}
                 [:span.fa.fa-pencil]]]])))]])))


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

        category-dropdown-id (str (gensym))

        dropdowns-attrs {:data-target "#"
                         :data-toggle "dropdown"
                         :aria-haspopup "true"
                         :aria-expanded "false"}

        dropdowns-li-style {:height "30px"
                            :text-align :center
                            :line-height :30px}
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
        {:style {:width :100%}}
        [:div.input-group-addon [:span.fa.fa-tags]]
        [:div.dropdown.input-group-addon
         {:style {:width "100px" :cursor :pointer :border-right 0}}
         [:span
          (assoc dropdowns-attrs :id category-dropdown-id)
          (or (:selected-type @state) "Type")
          "  "
          [:span.caret {:style {:margin-left :15px}}]]
         [:ul.dropdown-menu
          {:aria-labelledby category-dropdown-id
           :style {:min-width :100%}}
          (for [o (disj (set (:options @state)) (:selected-type @state))]
            ^{:key (gensym)}
            [:li {:style dropdowns-li-style
                  :on-click #(swap! state assoc :selected-type o :value-focus true)}
             o])]]
        [(bs-dropdown
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
            [:span.delete-button.fa.fa-times.btn.btn-default
             {:on-click #(swap! state update :values disj v)
              :style {:line-height :32px
                      :padding "0 10px"
                      :vertical-align :middle}}]
            [:span.btn.btn-default.value-type (singularize-type type)]
            [:span.btn.btn-default.value-txt value]]])]])))

(comment "previous attempts"

         (defn tags [{:keys [on-change style data]}]
           (let [state (atom {:selected nil
                              :options ["Queries" "Tags" "Tag-groups"]
                              :data data
                              :values #{}})
                 values-idx (cljs.core/atom 0)
                 current-choices #(clojure.set/difference
                                   (set (get-in @state [:data (when (:selected @state) (keyword (clojure.string/lower-case (:selected @state))))]))
                                   (set (map :value (:values @state))))
                 add-to-values #(swap! state update :values conj
                                       (with-meta {:type (:selected @state) :value %}
                                                  {:idx (inc @values-idx)}))

                 category-dropdown-id (str (gensym))
                 items-dropdown-id (str (gensym))

                 dropdowns-attrs {:data-target "#"
                                  :data-toggle "dropdown"
                                  :aria-haspopup "true"
                                  :aria-expanded "false"}

                 dropdowns-li-style {:height "30px"
                                     :text-align :center
                                     :line-height :30px}]

             (fn []
               [:div.tokens-container
                {:style style}
                [:div.input-group
                 {:style {:width :100%}}
                 [:div.input-group-addon [:span.fa.fa-tags]]
                 [:div.dropdown.input-group-addon
                  {:style {:width "100px" :cursor :pointer}}
                  [:span
                   (assoc dropdowns-attrs :id category-dropdown-id)
                   (or (:selected @state) "Type")
                   "  "
                   [:span.caret {:style {:margin-left :15px}}]]
                  [:ul.dropdown-menu
                   {:aria-labelledby category-dropdown-id
                    :style {:min-width :100%}}
                   (for [o (disj (set (:options @state)) (:selected @state))]
                     ^{:key (gensym)}
                     [:li {:style dropdowns-li-style
                           :on-click #(do (swap! state assoc :selected o))}
                      o])]]
                 [:div.dropdown.form-control
                  {:style {:cursor :pointer}}
                  [:span.dropdown-toggle
                   (assoc dropdowns-attrs :id items-dropdown-id :style {:min-width :100%})
                   "select "
                   [:span.caret {:style {:margin-left :15px}}]]

                  [:ul.dropdown-menu
                   {:aria-labelledby items-dropdown-id}
                   (for [o (current-choices)]
                     ^{:key (gensym)}
                     [:li {:style dropdowns-li-style
                           :on-click #(add-to-values o)}
                      o])]]]
                [:div.values
                 {:style {:padding-left :20px}}
                 (for [{:keys [type value] :as v} (sort-by (comp :idx meta) (:values @state))]
                   ^{:key (gensym)}
                   [:div
                    [:div.value.btn-group
                     [:span.delete-button.fa.fa-times.btn.btn-default
                      {:on-click #(swap! state update :values disj v)
                       :style {:line-height :32px
                               :padding "0 10px"
                               :vertical-align :middle}}]
                     [:span.btn.btn-default.value-type type]
                     [:span.btn.btn-default.value-txt value]]])]])))

         (defn tags* [{:keys [on-change style choices]}]
           (let [state (atom {:selected (name (first (keys choices)))
                              :options (mapv name (keys choices))
                              :input-focus false
                              :values #{}})
                 values-idx (cljs.core/atom 0)
                 input-el #(js/$ "input" ".tokens-container")
                 focus #(.focus (input-el))
                 blur #(.blur (input-el))
                 set-val #(.val (input-el) %)
                 get-val #(.val (input-el))
                 clear #(.val (input-el) "")
                 select-text #(.select (input-el))
                 trigger-panel #(let [e (js/$.Event "keyup")
                                      _ (set! (.-which e) 39)]
                                 (js/console.log e)
                                 (.trigger (input-el) e))]
             (r/create-class
               {:reagent-render
                (fn []
                  [:div.tokens-container
                   {:style style}
                   [:div.input-group
                    [:div.dropdown.input-group-addon
                     {:style {:width "100px"
                              :cursor :pointer}}
                     [:span#tags-dd
                      {:data-target "#"
                       :data-toggle "dropdown"
                       :aria-haspopup "true"
                       :aria-expanded "false"}
                      (:selected @state)]
                     [:ul.dropdown-menu
                      {:aria-labelledby "tags-dd"
                       :style {:min-width :100%}}
                      (for [o (disj (set (:options @state)) (:selected @state))]
                        ^{:key (gensym)}
                        [:li {:style {:height "30px"
                                      :text-align :center
                                      :line-height :30px}
                              :on-click #(do (swap! state assoc :selected o :input-focus true)
                                             (focus))}
                         o])]]
                    [:input.form-control.typeahead
                     {:type "text"
                      :on-focus #(swap! state assoc :input-focus true)
                      :on-blur #(swap! state assoc :input-focus false)
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
                         [:span.btn.btn-default.value-txt {:on-click edit} value]
                         (when (:hover @state) [:span.btn.btn-default.value-edit
                                                {:on-click edit}
                                                [:span.fa.fa-pencil]])]))]])
                :component-did-update
                (fn [this that]
                  (println "did update")
                  (.. (js/$ ".typeahead" (r/dom-node this))
                      (typeahead "destroy")
                      (typeahead
                        (clj->js {:source ((keyword (:selected @state)) choices)
                                  :minLength 0})))
                  (when (:input-focus @state) (trigger-panel)))})))

         (defn tags-sub-selector [state css-class item-name data-field]
           [:select.selectpicker
            {:class css-class
             :data-width "200px"
             :data-live-search "true"
             :title (str "select a " item-name)
             :style {:display (if (= css-class (:selected state)) :inline-block :none)}}
            (for [o (get-in state [:data data-field])]
              ^{:key (gensym)}
              [:option o])])

         (defn tags** [{:keys [on-change style data]}]
           (let [state (atom {:selected "Queries"
                              :options ["Queries" "Tags" "Tag-groups"]
                              :data data
                              :values #{}})
                 values-idx (cljs.core/atom 0)
                 hide-non-selected-dropdowns (fn [this]
                                               (.hide (js/$ ".bootstrap-select" (r/dom-node this)))
                                               (.show (js/$ (str ".bootstrap-select." (:selected @state))
                                                            (r/dom-node this))))
                 cat->data-key #(keyword (clojure.string/lower-case %))]
             (r/create-class
               {:reagent-render
                (fn []
                  [:div.tokens-container
                   {:style style}
                   [:div.input-group.btn-group
                    [:div.dropdown.input-group-addon
                     {:style {:width "100px"
                              :cursor :pointer}}
                     [:span#tags-dd
                      {:data-target "#"
                       :data-toggle "dropdown"
                       :aria-haspopup "true"
                       :aria-expanded "false"}
                      (:selected @state)]
                     [:ul.dropdown-menu
                      {:aria-labelledby "tags-dd"
                       :style {:min-width :100%}}
                      (for [o (disj (set (:options @state)) (:selected @state))]
                        ^{:key (gensym)}
                        [:li {:style {:height "30px"
                                      :text-align :center
                                      :line-height :30px}
                              :on-click #(do (swap! state assoc :selected o))}
                         o])]]
                    (tags-sub-selector @state "Queries" "query" :queries)
                    (tags-sub-selector @state "Tags" "tag" :tags)
                    (tags-sub-selector @state "Tag-groups" "tag-group" :tag-groups)]
                   [:div.values
                    (for [{:keys [type value] :as v} (sort-by (comp :idx meta) (:values @state))]
                      ^{:key (gensym)}
                      [:div.value.btn-group
                       [:span.delete-button.fa.fa-times.btn.btn-default
                        {:on-click #(swap! state update :values disj v)
                         :style {:line-height :32px
                                 :padding "0 10px"
                                 :vertical-align :middle}}]
                       [:span.btn.btn-default.value-type type]
                       [:span.btn.btn-default.value-txt value]])]])
                :component-did-update
                hide-non-selected-dropdowns
                :component-did-mount
                (fn [this]
                  (.selectpicker (js/$ ".selectpicker" (r/dom-node this))
                                 (clj->js {:style "btn btn-default"
                                           :size 4}))
                  (.on (js/$ ".selectpicker" (r/dom-node this))
                       "changed.bs.select"
                       (fn [e idx]
                         (swap! state update :values conj
                                (with-meta {:type (:selected @state)
                                            :value (get-in @state [:data (cat->data-key (:selected @state)) (dec idx)])}
                                           {:idx (inc @values-idx)}))))
                  (hide-non-selected-dropdowns this))})))
         )