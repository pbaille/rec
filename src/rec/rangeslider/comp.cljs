(ns rec.rangeslider.comp
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [reagent.core :as r :refer [atom]]))

(defn- interp [[x1 y1] [x2 y2] x]
  (let [diff1 (- y1 x1)
        diff2 (- y2 x2)
        diffx (- x x1)
        rat (/ diffx diff1)]
    (+ x2 (* rat diff2))))

(defn- bounded [[min max] x]
  (cond
    (> x max) max
    (< x min) min
    :else x))

(defn- round [x n]
  (let [pd (js/Math.pow 10 n)]
    (/ (js/Math.round (* x pd))
       pd)))

(round 1.234 2)

(defn rangeslider [{:keys [height range size plot-size on-change] :as opts}]
  (let [id (gensym)
        state (atom {:plot1-dragged false
                     :plot1-x 0
                     :plot2-dragged false
                     :plot2-x size
                     :x-offset 0})

        values (reaction (mapv (partial interp [0 size] range) [(:plot1-x @state) (:plot2-x @state)]))
        active (reaction (or (:plot1-dragged @state) (:plot2-dragged @state)))

        assoc-state! (fn [kw bool] #(swap! state assoc kw bool))

        stop! #(when @active
                (swap! state assoc :plot1-dragged false :plot2-dragged false)
                (on-change @values))]

    (r/create-class
      {:reagent-render
       (fn []
         (let [[min max] @values
               {:keys [plot1-x plot2-x x-offset]} @state]
           [:div.rangeslider-container
            {:class (when @active "active")
             :on-mouse-up stop!
             :on-mouse-leave stop!
             :on-mouse-move
             (fn [e]
               (cond
                 (:plot1-dragged @state)
                 (swap! state assoc :plot1-x (bounded [0 (:plot2-x @state)]
                                                      (- (- (.. e -nativeEvent -clientX)
                                                            (:x-offset @state))
                                                         (/ plot-size 2))))
                 (:plot2-dragged @state)
                 (swap! state assoc :plot2-x (bounded [(:plot1-x @state) size]
                                                      (+ (- (.. e -nativeEvent -clientX)
                                                            (:x-offset @state))
                                                         (/ plot-size 2))))
                 :else nil))}

            [:div.rangeslider-track
             {:id (str id)
              :style {:height (str height "px")
                      :width (str size "px")}}

             [:div.plot.plot1
              {:on-mouse-down (assoc-state! :plot1-dragged true)
               :on-mouse-up stop!
               :style {:width (str plot-size "px")
                       :height (str height "px")
                       :left (- (+ x-offset plot1-x) plot-size)}}
              [:div.plot-label (round min 2)]]

             [:div.inside {:style {:left (+ x-offset plot1-x)
                                   :width (str (.abs js/Math (- plot1-x plot2-x)) "px")
                                   :height height}}]

             [:div.plot.plot2
              {:on-mouse-down (assoc-state! :plot2-dragged true)
               :on-mouse-up stop!
               :style {:width (str plot-size "px")
                       :height (str height "px")
                       :left (+ x-offset plot2-x)}}
              [:div.plot-label (round max 2)]]]]))

       :component-did-mount
       #(swap! state assoc :x-offset (.-offsetLeft (.getElementById js/document id)))})))

(defn multi-rangeslider [{:keys [height range size plot-size on-change] :as opts}]
  (let [state (atom {:rangesliders {}
                     :value {}})]
    (fn []
      [:div.multi-rangeslider
       [:div.add-rangeslider
        {:on-click
         (fn []
           (let [sym (gensym)]
             (swap! state
                    assoc-in
                    [:rangesliders sym]
                    ^{:key sym}
                    [rangeslider (assoc opts :on-change
                                             #(do (swap! state assoc-in [:value sym] %)
                                                  (on-change (:value @state))))])))}
        [:i.zmdi.zmdi-plus-circle-o]
        [:span "add rangeslider"]]
       (for [[sym rs] (:rangesliders @state)] rs)])))

