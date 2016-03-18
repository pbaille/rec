(ns rec.daterange.comp
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [reagent.core :as r]
            [re-com.core :as rc]
            [cljs-time.core :as t]
            [cljs-time.format :refer [parse unparse formatter]]
            [rec.timepicker.comp :refer [timepicker]]))

(enable-console-print!)

(t/date? (t/today-at-midnight))

(defn parse* [x]
  (parse (formatter "yyyyMMDDHHmmss") x))

(defn unparse* [x]
  (unparse (formatter "yyyy/MM/DD HH:mm:ss") x))

(defn hm->date [{:keys [year month day hours minutes seconds] :as hm}]
  (assert (every? identity (map #(% hm) [:year :month :day :hours :minutes :seconds])))
  (parse* (str year month day hours minutes seconds)))

(defn str->date [s]
  (cond
    (<= 8 (count (seq s))) (parse* (apply str (take 14 (concat (seq s) (repeat \0)))))
    (= 14 (count (seq s))) (parse* s)
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
    (t/date? x) x
    (map? x) (hm->date x)
    (string? x) (str->date x)
    :else (js/Error. (str "cannot format date: " x))))

(defn time-input-value->hm [x]
  (let [[hours minutes seconds]
        (map #(apply str (butlast %))
             (partition 3 3 ":" x))]
    {:hours hours
     :minutes minutes
     :seconds seconds}))

(defn at
  "set the hours of the given date"
  ([date hours minutes seconds millis]
   (doto date
     (.setHours hours)
     (.setMinutes minutes)
     (.setSeconds seconds)
     (.setMilliseconds millis)))
  ([date hours minutes seconds]
   (at date hours minutes seconds 0))
  ([date hours minutes]
   (at date hours minutes 0)))

(defn assoc-time [date {:keys [hours minutes seconds]}]
  (at date hours minutes seconds))

(defn presets []
  (let [end-of-today (t/today-at 23 59 59 999)]
    [[:today [(t/today-at-midnight) end-of-today]]
     [:yesterday [(t/at-midnight (t/yesterday)) (at (t/yesterday) 23 59 59 999)]]
     [:last-hour [(t/ago (t/hours 1)) (t/now)]]
     [:last-48h [(t/ago (t/days 2)) (t/now)]]
     [:last-week [(t/ago (t/weeks 1)) (t/now)]]
     [:last-month [(t/ago (t/months 1)) (t/now)]]
     [:last-year [(t/ago (t/years 1)) (t/now)]]]))

(defn t>=
  "same as t/after? but return true if t1 and t2 are simultaneous"
  [t1 t2]
  (if (t/after? t1 t2)
    true
    (not (t/before? t1 t2))))

(defn t<=
  "same as t/before? but return true if t1 and t2 are simultaneous"
  [t1 t2]
  (if (t/before? t1 t2)
    true
    (not (t/after? t1 t2))))

(comment "tests"
         (t>= (t/today-at-midnight) (t/today-at 12 12))
         (t>= (t/today-at 12 12) (t/today-at-midnight))
         (t<= (t/today-at-midnight) (t/today-at-midnight))

         (t<= (t/today-at-midnight) (t/today-at 12 12))
         (t<= (t/today-at 12 12) (t/today-at-midnight))
         (t<= (t/today-at-midnight) (t/today-at-midnight)))

(defn daterange [{:keys [from to on-change preset]}]
  (let [state (r/atom {:from (format-date from)
                       :to (format-date to)
                       :open false
                       :preset nil})
        from (r/cursor state [:from])
        to (r/cursor state [:to])
        from-hm (reaction (date->hm @from))
        to-hm (reaction (date->hm @to))
        value (reaction {:from @from :to @to :from-hm @from-hm :to-hm @to-hm})
        from-timepicker-cb #(swap! state update :from assoc-time (time-input-value->hm %))
        to-timepicker-cb #(swap! state update :to assoc-time (time-input-value->hm %))]
    (fn []
      [:div.daterange
       [:div.presets
        (for [[n [f t]] (presets)]
          ^{:key (gensym)}
          [:span.preset {:on-click #(do (swap! state assoc :from f :to t :preset n)
                                        (on-change @value))}
           (name n)])]
       [:div.datefrom
        [(fn []
           [rc/datepicker
            :on-change (fn [x]
                         (let [date-from (assoc-time x (date->hm @from))]
                           (if (t/before? @to date-from)    ;; when same day is selected ensure that hour coherent
                             (swap! state update :from #(assoc-time x (date->hm @to)))
                             (swap! state assoc :from date-from)))
                         (on-change @value))
            :show-today? true
            :selectable-fn (partial t>= (t/at-midnight @to))
            :model from
            :hide-border? true])]
        [(timepicker (merge {:on-blur from-timepicker-cb
                             :on-return from-timepicker-cb}
                            @from-hm))]]
       [:div.dateto
        [(fn []
           [rc/datepicker
            :on-change (fn [x]
                         (let [date-to (assoc-time x (date->hm @to))]
                           (if (t/before? date-to @from) ;; when same day is selected ensure that hour coherent
                             (swap! state update :to #(assoc-time x (date->hm @from)))
                             (swap! state assoc :to date-to)))
                         (on-change @value))
            :show-today? true
            :selectable-fn (partial t<= (t/at-midnight @from))
            :model to
            :hide-border? true])]
        [(timepicker (merge {:on-blur to-timepicker-cb
                             :on-return to-timepicker-cb}
                            @to-hm))]]])))

;; timeperiod picker ---------------------------------------

(defn- input-helper [state kw on-change]
  ^{:key (gensym)}
  [:span.period-section
   [:input {:style {:width "30px"
                    :border 0
                    :outline :none
                    :text-align :right}
            :class (name kw)
            :type "number"
            :default-value (or (kw @state) 0)
            :min 0
            :on-change #(do (swap! state assoc kw (.. % -target -value))
                            (on-change @state))}]
   [:span (name kw)]])

(defn time-period [opts]
  (let [state (atom (t/map->Period (:period opts)))]
    (fn []
      [:div.sliding-timerange-selector
       (for [kw [:years :months :days :hours :minutes :seconds]]
         (input-helper state kw (:on-change opts identity)))])))