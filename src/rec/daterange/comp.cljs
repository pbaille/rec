(ns rec.daterange.comp
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [reagent.core :as r]
            [re-com.core :as rc]
            [cljs-time.core :as t]
            [cljs-time.format :refer [parse unparse formatter]]
            [rec.timepicker.comp :refer [timepicker]]))

(enable-console-print!)

(println "welcome to curly template")

(defn parser [x]
  (parse (formatter "yyyyMMDDHHmmss") x))

(defn hm->date [{:keys [year month day hours minutes seconds] :as hm}]
  (assert (every? identity (map #(% hm) [:year :month :day :hours :minutes :seconds])))
  (parser (str year month day hours minutes seconds)))

(defn str->date [s]
  (cond
    (<= 8 (count (seq s))) (parser (apply str (take 14 (concat (seq s) (repeat \0)))))
    (= 14 (count (seq s))) (parser s)
    :else (js/Error. "unknown date string format")))

(defn date->hm [date]
  (let [d (str date)]
    {:year (subs d 0 4)
     :month (subs d 4 6)
     :day (subs d 6 8)
     :hours (subs d 9 11)
     :minutes (subs d 11 13)
     :seconds (subs d 13)}))

(comment
  (date->hm (str->date "20160101123456"))
  (hm->date {:year "2016"
             :month "12"
             :day "12"
             :hours "00"
             :minutes "00"
             :seconds "00"}))

(defn format-date [x]
  (cond
    (map? x) (hm->date x)
    (string? x) (str->date x)
    :else (js/Error. (str "cannot format date: " x))))

(defn time-input-value->hm [x]
  (let [[hours minutes seconds] (map #(apply str (butlast %))
                                     (partition 3 3 ":" x))]
    {:hours hours
     :minutes minutes
     :seconds seconds}))

(defn assoc-time [d time]
  (let [time-hm (if (string? time) (time-input-value->hm time) time)
        {:keys [hours minutes seconds]} time-hm]
    (str->date
      (str
        (subs (str d) 0 8)
        hours minutes seconds))))

(defn assoc-time! [d time-string]
  (reset! d (assoc-time @d time-string)))

(defn presets []
  (let [midnight (t/today-at 23 59 59)]
    [[:today [(t/today-at-midnight) midnight]]
     [:yesterday [(t/at-midnight (t/yesterday)) (t/today-at-midnight)]]
     [:last-hour [(t/ago (t/hours 1)) midnight]]
     [:last-48h [(t/ago (t/days 2)) midnight]]
     [:last-week [(t/ago (t/weeks 1)) midnight]]
     [:last-month [(t/ago (t/months 1)) midnight]]
     [:last-year [(t/ago (t/years 1)) midnight]]]))


(defn daterange [{:keys [from to on-change]}]
  (let [state (r/atom {:from (format-date from)
                       :to (format-date to)
                       :open false})
        from (r/cursor state [:from])
        to (r/cursor state [:to])
        from-hm (reaction (date->hm @from))
        to-hm (reaction (date->hm @to))
        value (reaction {:from @from :to @to :from-hm @from-hm :to-hm @to-hm})
        from-timepicker-cb #(swap! state update :from assoc-time %)
        to-timepicker-cb #(swap! state update :to assoc-time %)]
    (fn []
      [:div.daterange
       [:div.presets
        (for [[n [f t]] (presets)]
          ^{:key (gensym)}
          [:span.preset {:on-click #(swap! state assoc :from f :to t)}
           (name n)])]
       [:div.datefrom
        [(fn []
           [rc/datepicker
            :on-change (fn [x]
                         (swap! state update :from #(assoc-time x (date->hm %)))
                         (on-change @value))
            :show-today? true
            :selectable-fn (fn [x] (<= (.valueOf x) (.valueOf @to)))
            :model from
            :hide-border? true])]
        [(timepicker (merge {:on-blur from-timepicker-cb
                             :on-return from-timepicker-cb}
                            @from-hm))]]
       [:div.dateto
        [(fn []
           [rc/datepicker
            :on-change (fn [x]
                         (swap! state update :to #(assoc-time x (date->hm %)))
                         (on-change @value))
            :show-today? true
            :selectable-fn (fn [x] (>= (.valueOf x) (.valueOf @from)))
            :model to
            :hide-border? true])]
        [(timepicker (merge {:on-blur to-timepicker-cb
                             :on-return to-timepicker-cb}
                            @to-hm))]]])))

