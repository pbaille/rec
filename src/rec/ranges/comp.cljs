(ns rec.ranges.comp
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [reagent.core :as r :refer [atom]]))

(defn simple-range [{:keys [on-change value op]
                     :or {value 0}
                     :as opts}]
  (let [op-str (name op)
        state (atom {:op op-str :value value})]
    (fn []
      [:div.simplerange {:class op-str}
       [:span op-str]
       [:input (merge {:type "number"
                       :default-value value
                       :on-change #(do
                                    (swap! state assoc :value (.. % -target -value))
                                    (on-change @state))}
                      (dissoc opts :on-change :value :op))]])))

(defn range* [{:keys [on-change value op]
               :or {value {:from 0 :to 0}
                    on-change identity}
               :as opts}]
  (let [op-str (name op)
        state (atom {:op op-str :value value})
        on-from-change (fn [from]
                         (let [to (get-in @state [:value :to])
                               move-to? (> from to)]
                           (swap! state assoc :value {:from from :to (if move-to? from to)})
                           (on-change @state)))
        on-to-change (fn [to]
                       (let [from (get-in @state [:value :from])
                             move-from? (> from to)]
                         (swap! state assoc :value {:from (if move-from? to from) :to to})
                         (on-change @state)))
        cleaned-opts (dissoc opts :on-change :value :op)]
    (fn []
      [:div.range {:class op-str}
       [:span (str op-str "  ")]
       (for [[k v] (:value @state)]
         ^{:key k}
         [:input (merge {:type "number"
                         :value v
                         :on-change (condp = k
                                      :to #(on-to-change (.. % -target -value))
                                      :from #(on-from-change (.. % -target -value)))}
                        cleaned-opts)])])))

(defn gt [opts] (simple-range (merge {:op  :>} opts)))
(defn gte [opts] (simple-range (merge {:op :>=} opts)))
(defn lt [opts] (simple-range (merge {:op  :<} opts)))
(defn lte [opts] (simple-range (merge {:op :<=} opts)))
(defn in [opts] (range* (merge {:op :in} opts)))
(defn out [opts] (range* (merge {:op :out} opts)))

(defn- get-default-value [{:keys [min max]} name]
  (get {">" min
        ">=" min
        "<" max
        "<=" max
        "in" {:from min :to max}
        "out" {:from 0 :to 0}}
       name))

(defn multirange [{:keys [on-change placeholder]
                   :or {placeholder "add a range"
                        on-change identity}
                   :as opts}]
  (let [state (atom {:ranges {}
                     :focus false
                     :value {}})
        opts (dissoc opts :on-change)
        names->components {">" gt ">=" gte "<" lt "<=" lte "in" in "out" out}]
    (fn []
      (let [focus? (:focus @state)]
        [:div.multirange-container
         (if-not focus?
           [:div.add {:on-click #(swap! state assoc :focus true)}
            [:i.zmdi.zmdi-plus-circle-o]
            [:span.placeholder placeholder]]
           [:div.constructors
            (for [[name component] names->components]
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
                                          [component (assoc opts
                                                       :value default-value
                                                       :on-change
                                                       (fn [x]
                                                         (swap! state update :value assoc sym x)
                                                         (on-change (:value @state))))])
                                  (update :value assoc sym {:op name :value default-value}))))
                    (on-change (:value @state)))}
                 name]))])

         [:div.ranges
          (for [[sym comp] (:ranges @state)]
            ^{:key sym}
            [:div.ranges-item comp [:i.zmdi.zmdi-close-circle-o
                                    {:on-click #(swap! state update :ranges dissoc sym)}]])]]))))