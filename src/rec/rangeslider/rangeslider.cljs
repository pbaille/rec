(ns rec.rangeslider.rangeslider
  (:require [reagent.core :as r :refer [atom]]))

(defn plot-style [& xs]
  (apply merge {:background :lightgrey :display :inline-block} xs))

(defn rangeslider [{:keys [height from to size plot-size]}]
  (let [id (gensym)
        state (atom {:plot1-dragged false
                     :plot1-x 0
                     :plot2-dragged false
                     :plot2-x size
                     :x-offset 0})
        drag (fn [kw bool] #(swap! state assoc kw bool))
        init #(do (swap! state assoc :x-offset (.-offsetLeft (.getElementById js/document id)))
                  )
        ]
    (r/create-class
      {:reagent-render (fn []
                         [:div.rangeslider-container
                          {:id (str id)
                           :on-click (fn [e] #_(set! (.. e -target -offsetLeft) (+ 10 (.. e -target -offsetLeft))))
                           :on-mouse-move (fn [e]
                                            (cond
                                              (:plot1-dragged @state)
                                              (swap! state assoc :plot1-x (- (- (.. e -nativeEvent -clientX)
                                                                                (:x-offset @state))
                                                                             (/ plot-size 2)))
                                              (:plot2-dragged @state)
                                              (swap! state assoc :plot2-x (+ (- (.. e -nativeEvent -clientX)
                                                                                (:x-offset @state))
                                                                             (/ plot-size 2)))
                                              :else "yop"))
                           :on-mouse-leave #(do (swap! state assoc :plot1-dragged false :plot2-dragged false))
                           :style {:box-sizing :border-box
                                   :background :#FAFAFA
                                   :height (str height "px")
                                   :width (str size "px")}}
                          [:div.plot.plot1
                           {:on-mouse-down (drag :plot1-dragged true)
                            :on-mouse-up (drag :plot1-dragged false)
                            :style (plot-style {:width (str plot-size "px")
                                                :height (str height "px")
                                                :position :absolute
                                                :left (+ (:x-offset @state) (:plot1-x @state))})}]
                          [:div.inside {:style {:display :inline-block
                                                :background :lightskyblue
                                                :position :absolute
                                                :left (+ (:x-offset @state) (:plot1-x @state) plot-size)
                                                :width (str (- (.abs js/Math (- (:plot1-x @state) (:plot2-x @state))) (* 2 plot-size)) "px")
                                                :height height}}]
                          [:div.plot.plot2
                           {:on-mouse-down (drag :plot2-dragged true)
                            :on-mouse-up (drag :plot2-dragged false)
                            :style (plot-style {:width (str plot-size "px")
                                                :height (str height "px")
                                                :position :absolute
                                                :left (- (+ (:x-offset @state) (:plot2-x @state))
                                                         plot-size)})}]])
       :component-did-mount init})))

(r/render-component [rangeslider {:from 0 :to 10 :size 400 :plot-size 20 :height 20}]
                    (.getElementById js/document "rangeslider"))

