(ns rec.multirangeslider.comp
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [reagent.core :as r :refer [atom]]))

(defn rangeslider [{:keys [min max on-validate on-delete]}]
  (let [state (atom {:edit true
                     :value {:from min :to max}})]
    (r/create-class
      {:reagent-render
       (fn []
         (let [edit? (:edit @state)]
           [:div.bs-rangeslider-container.btn-group
            [:div.btn.btn-default
             {:on-click #(on-delete (:value @state))}
             [:span.fa.fa-times]
             [:span.range-head "Range:"]]
            [:div.btn.btn-default.center-zone
             [:span.bound.from-value
              {:style (if edit? {:padding-right :15px}
                                {:padding :6px})}
              (if edit? min (get-in @state [:value :from]))]
             (when-not edit? [:i.fa.fa-arrow-right {:style {:padding-left :4px}}])
             [:div.input-wrap {:style {:display (if edit? :inline-block :none)}}
              [:input
               {:type "text"
                :class "span2"
                :value ""
                :data-slider-min min
                :data-slider-max max
                :data-slider-value (str "[" min "," max "]")}]]
             [:span.bound.to-value
              {:style (if edit? {:padding-left :15px}
                                {:padding :6px})}
              (if edit? max (get-in @state [:value :to]))]]

            [:div.btn.btn-default (if edit? [:span.fa.fa-check {:on-click #(do (swap! state assoc :edit false)
                                                                               (on-validate (:value @state)))}]
                                            [:span.fa.fa-pencil {:on-click #(swap! state assoc :edit true)}])]]))
       :component-did-mount
       (fn [this]
         (.on (.slider (js/$ "input" (r/dom-node this)))
              "slide" #(swap! state
                              assoc
                              :value
                              {:from (aget (.-value %) 0)
                               :to (aget (.-value %) 1)})))})))

(defn multi-rangeslider
  [{:keys [min max style placeholder values on-change]
    :or {placeholder "multi-rangeslider"}}]
  (let [state (atom {:sliders {}
                     :hover false})]
    (fn []
      [:div.multi-bsrs-container
       [:div.btn-group
        {:on-mouse-enter #(swap! state assoc :hover true)
         :on-mouse-leave #(swap! state assoc :hover false)}
        [:div.btn.btn-default [:i.fa.fa-times]]
        [:div.btn.btn-default placeholder]
        (when (:hover @state)
          (let [sym (gensym)]
            [:div.btn.btn-default
             {:on-click
              (fn [_]
                (swap! state
                       update
                       :sliders
                       assoc
                       sym
                       {:value {:from min :to max}
                        :comp
                        ^{:key sym}
                        [rangeslider {:min min
                                      :max max
                                      :on-delete
                                      #(do (swap! state update :sliders dissoc sym)
                                           (on-change (map :value (vals (:sliders @state)))))
                                      :on-validate
                                      #(do (swap! state update :sliders assoc-in [sym :value] %)
                                           (on-change (map :value (vals (:sliders @state)))))}]}))}
             [:span.fa.fa-plus]]))]
       [:div.ranges
        {:style {:padding :20px}}
        (for [[s {:keys [comp]}] (:sliders @state)]
          comp)]])))
