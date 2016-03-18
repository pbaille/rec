(ns rec.sidebar
  (:require [reagent.core :refer [atom] :as r]
            [rec.daterange.comp :refer [daterange unparse*]]
            [rec.bs-multiselect.comp :refer [bs-multiselect]]
            [rec.tokens.comp :refer [tokens tags]]
            [rec.multitoggle.comp :refer [multitoggle]]
            [rec.multirangeslider.comp :refer [multi-rangeslider]]
            [cljs-time.core :as t]))

;; helpers ----------------------------------------------

(defn comp-selector [{:keys [icon-class comps on-change]}]
  (let [state (atom {:comps comps
                     :selected #{}})
        selected? #((:selected @state) (key %))]
    (fn []
      [:div.comp-selector
       [:div.input-group
        [:div.input-group-addon
         [:i.fa {:class icon-class}]]
        [:div.dropdown.form-control
         {:style {:cursor :pointer}}
         [:div
          {:data-target "#"
           :data-toggle "dropdown"
           :aria-haspopup "true"
           :aria-expanded "false"}
          "New Author filter..."]
         [:ul.dropdown-menu
          {:aria-labelledby "tokens-dd"}
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
          (for [[_ comp] (filter selected? (:comps @state))]
            ^{:key (gensym)}
            comp))]])))

(defn label [s]
  [:div
   {:style {:font-size :20px
            :padding-bottom :10px}}
   s])

(defn categorical-dropdown []
  )

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
        [:div.input-group-addon [:a.fa.fa-calendar]]
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
     :comps
     {:Age [multi-rangeslider {:placeholder "Age" :min 0 :max 100 :on-change #(println %)}]
      :Klout [multi-rangeslider {:placeholder "Klout" :min 0 :max 100 :on-change #(println %)}]
      :Gender [multitoggle {:title "Gender" :xs ["male" "female"] :max-selected 1 :on-change #(println %)}]}}]])

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
     :comps
     {:Mood [multitoggle {:title "Mood" :xs ["positive" "negative" "factual"] :max-selected 2 :on-change #(println %)}]
      :Language [bs-multiselect {:placeholder "Add a language filter" :data ["French" "English" "German"] :on-change #(println %)}]
      :Gender [multitoggle {:title "Gender" :xs ["male" "female"] :max-selected 1 :on-change #(println %)}]}}]])


(defn sidebar []
  [:div.sidebar
   {:style {:width :553px
            :padding :10px}}
   [period {}]
   [keywords]
   [:div.tags]
   [tags*]
   [authors]
   [text-content]
   [:div.text-content]
   [:div.media-content]])

(r/render-component [sidebar]
                    (.getElementById js/document "app"))



