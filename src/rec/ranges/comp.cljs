(ns rec.ranges.comp
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [reagent.core :as r :refer [atom]]))

(defn simple-range [kw {:keys [on-change value]
                        :or {value 0}
                        :as opts}]
  [:div.simplerange {:class (name kw)}
   [:span (name kw)]
   [:input (merge {:type "number"
                   :default-value value
                   :on-change #(on-change (.. % -target -value))}
                  (dissoc opts :on-change :value))]])

(defn range* [kw {:keys [on-change from to]
                  :or {from 0 to 0}
                  :as opts}]
  (let [state (atom {:from from :to to})
        on-from-change (fn [from]
                         (let [to (:to @state)
                               move-to? (> from to)]
                           (swap! state assoc :from from :to (if move-to? from to))
                           (on-change @state)))
        on-to-change (fn [to]
                       (let [from (:from @state)
                             move-from? (> from to)]
                         (swap! state assoc :from (if move-from? to from) :to to)
                         (on-change @state)))]
    (fn []
      [:div.range {:class (name kw)}
       [:span (str (name kw) "  ")]
       [:input (merge {:type "number"
                       :value (:from @state)
                       :on-change #(on-from-change (.. % -target -value))}
                      (dissoc opts :on-change :from :to))]
       [:input (merge {:type "number"
                       :value (:to @state)
                       :on-change #(on-to-change (.. % -target -value))}
                      (dissoc opts :on-change :from :to))]])))

(def gt (partial simple-range :>))

(def gte (partial simple-range :>=))

(def lt (partial simple-range :<))

(def lte (partial simple-range :<=))

(def in (partial range* :in))

(def out (partial range* :out))

(defn- get-default-value [{:keys [min max]} name]
  (get {">" {:value min}
        ">=" {:value min}
        "<" {:value max}
        "<=" {:value max}
        "in" {:from min :to max}
        "out" {:from 0 :to 0}}
       name))

(defn multirange [{:keys [on-change] :as opts}]
  (let [state (atom {:ranges {}
                     :focus false
                     :value {}})
        opts (dissoc opts :on-change)]
    (fn []
      (let [focus? (:focus @state)]
        [:div.multirange-container
         (if-not focus?
           [:div.add {:on-click #(swap! state assoc :focus true)}
            [:span "add a range"]]
           [:div.constructors
            (for [[name component] {">" gt ">=" gte "<" lt "<=" lte "in" in "out" out}]
              (let [sym (gensym)]
                ^{:key sym}
                [:span.constructor
                 {:on-click
                  (fn []
                    (let [default-value (get-default-value opts name)]
                      (reset! state
                              (-> @state
                                  (update :focus not)
                                  (update :ranges
                                          assoc
                                          sym
                                          ^{:key (gensym)}
                                          [component (merge
                                                       default-value
                                                       (assoc opts
                                                         :on-change
                                                         (fn [x]
                                                           (swap! state update :value assoc sym x)
                                                           (on-change (:value @state)))))])
                                  (update :value assoc sym (:value default-value default-value)))))
                    (on-change (:value @state)))}
                 name]))])

         [:div.ranges
          (for [[sym comp] (:ranges @state)]
            ^{:key sym}
            [:div.ranges-item comp [:i.zmdi.zmdi-close-circle-o
                                    {:on-click #(swap! state update :ranges dissoc sym)}]])]]))))