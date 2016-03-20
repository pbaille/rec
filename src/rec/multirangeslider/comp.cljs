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
           [:div.bs-rangeslider-container
            [:div.btn-group
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
                 :value ""
                 :data-slider-min min
                 :data-slider-max max
                 :data-slider-value (str "[" min "," max "]")}]]
              [:span.bound.to-value
               {:style (if edit? {:padding-left :15px}
                                 {:padding :6px})}
               (if edit? max (get-in @state [:value :to]))]]

             [:div.btn.btn-default {:on-click (if edit?
                                                #(do (swap! state assoc :edit false)
                                                     (on-validate (:value @state)))
                                                #(swap! state assoc :edit true))}
              (if edit? [:span.fa.fa-check]
                        [:span.fa.fa-pencil])]]]))
       :component-did-mount
       (fn [this]
         (.on (.slider (js/$ "input" (r/dom-node this)))
              "slide" #(swap! state
                              assoc
                              :value
                              {:from (aget (.-value %) 0)
                               :to (aget (.-value %) 1)})))})))

(defn multi-rangeslider
  [{:keys [min max style placeholder values on-change on-delete icon-class]
    :or {placeholder "multi-rangeslider"}}]
  (let [state (atom {:sliders {}})
        sliders (reaction (seq (:sliders @state)))]
    (fn []
      [:div.multi-bsrs-container
       (when-not @sliders {:style {:margin-bottom :20px}})
       [:div.btn-group
        [:div.btn.btn-default.icon [:i {:class icon-class}]]
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
           placeholder])
        [:div.btn.btn-default.delete-button {:on-click on-delete} [:i.fa.fa-times]]]
       (when @sliders
         [:div.ranges
          {:style {:padding "20px"}}
          (for [[s {:keys [comp]}] @sliders]
            ^{:key (gensym)}
            comp)])])))
