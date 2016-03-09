(ns rec.timepicker.comp
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [reagent.core :as r :refer [atom]]))

(defn to-2digit-str [number]
  (cond
    (and (string? number) (= 2 (count (seq number)))) number
    (< number 10) (str "0" number)
    (> 100 number 9) (str number)
    :else nil))

(defn timepicker [{:keys [hours minutes seconds on-change on-blur on-return]
                   :as opts
                   :or {hours 0
                        minutes 0
                        seconds 0
                        on-change identity
                        on-blur identity
                        on-return identity}}]
  (let [v (str (to-2digit-str hours) ":" (to-2digit-str minutes) ":" (to-2digit-str seconds))]
    (fn []
      [:input.timepicker
       {:type "time"
        :step "1"
        :default-value v
        :on-key-down #(when (= 13 (.-which %)) (on-return (.. % -target -value)))
        :on-change #(on-change (.. % -target -value))
        :on-blur #(on-blur (.. % -target -value))}])))
