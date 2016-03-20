(ns rec.sidebar
  (:require [reagent.core :refer [atom] :as r]
            [rec.daterange.comp :refer [daterange unparse*]]
            [rec.bs-multiselect.comp :refer [bs-multiselect]]
            [rec.tokens.comp :refer [tokens tags]]
            [rec.multitoggle.comp :refer [multitoggle]]
            [rec.multirangeslider.comp :refer [multi-rangeslider]]
            [cljs-time.core :as t]))

;; helpers ----------------------------------------------

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

(defn label [s]
  [:div
   {:style {:font-size :20px
            :padding-bottom :10px}}
   s])

;; sub components ------------------------------------------

(defn period [{:keys [from to on-change]
               :or {from (t/today-at-midnight)
                    to (t/today-at 23 59 59 999)}}]
  (let [state (atom {:open false
                     :from from
                     :to to})]
    (fn []
      [:div.timerange-filter-container
       {:style {:width :100%
                :margin-bottom :20px}}
       [label "Period"]
       [:div.timerange-main-bar.input-group
        {:style {:width :100%}
         :on-click #(swap! state update :open not)}
        [:div.input-group-addon [:i.fa.fa-calendar]]
        [:div.form-control (str (unparse* (:from @state)) " -> " (unparse* (:to @state)))]]
       (when (:open @state)
         [daterange
          {:on-change #(do (swap! state assoc :from (:from %) :to (:to %))
                           (on-change (select-keys @state [:from :to])))
           :from (:from @state)
           :to (:to @state)}])])))

(defn keywords []
  [:div.keywords-container
   [label "Keywords"]
   [tokens {:style {:margin-bottom "20px"}
            :on-change #(println %)}]])

(defn authors []
  [:div.authors-container
   [label "Authors"]
   [comp-selector
    {:icon-class :fa-user
     :placeholder "New Author filter..."
     :comps
     {:Age ^{:key (gensym)} [multi-rangeslider {:icon-class "fa fa-birthday-cake" :placeholder "New age filter" :min 0 :max 100 :on-change #(println %)}]
      :Klout ^{:key (gensym)} [multi-rangeslider {:icon-class "fa fa-thumbs-o-up" :placeholder "New klout filter" :min 0 :max 100 :on-change #(println %)}]
      :Gender ^{:key (gensym)} [multitoggle {:icon-class "fa fa-venus-mars" :title "Gender" :xs ["male" "female"] :max-selected 1 :on-change #(println %)}]}}]])

(defn tags* []
  [:div.tags-container
   [label "Tags"]
   [tags {:style {:margin-bottom "20px"}
          :on-change #(println %)
          :data {:queries ["q1" "q2" "q3"]
                 :tags ["t1" "t2" "t3"]
                 :tag-groups ["tg1" "tg2" "tg3"]}}]])

(defn text-content []
  [:div.authors-container
   [label "Text Content"]
   [comp-selector
    {:icon-class :fa-font
     :placeholder "New content filter..."
     :comps
     {:Mood
      ^{:key (gensym)} [multitoggle {:title "Mood"
                                     :xs ["positive" "negative" "factual"]
                                     :max-selected 2
                                     :icon-class "fa fa-smile-o"
                                     :on-change #(println %)}]
      :Language
      ^{:key (gensym)} [bs-multiselect {:icon-class "fa fa-globe"
                                        :placeholder "Add a language filter"
                                        :data ["French" "English" "German"]
                                        :on-change #(println %)}]
      :Brands
      ^{:key (gensym)} [bs-multiselect {:icon-class "fa fa-copyright"
                                        :placeholder "Add a brand filter"
                                        :data ["Pepsi" "Chanel" "Fleuri michon" "Durex"]
                                        :on-change #(println %)}]}}]])


(defn sidebar []
  [:div.sidebar
   {:style {:width :553px
            :padding :10px}}
   [period {:on-change #(println %)}]
   [keywords]
   [:div.tags]
   [tags*]
   [authors]
   [text-content]
   [:div.text-content]
   [:div.media-content]])

(r/render-component [sidebar]
                    (.getElementById js/document "app"))



