(ns rec.wgroup
  (:require [reagent.core :refer [atom] :as r]))

(enable-console-print!)

(defn tree []
  (let [state (atom {})]
    (add-watch state :state #(println (deref %2)))
    (r/create-class
      {:reagent-render
       (fn []
         (println "rerender")
         (let [component (r/current-component)
               props (r/props component)
               childrens (r/children component)]
           [:div.tree
            props
            (doall
              (for [child childrens]
                ((:render child) state)))]))
       :component-did-mount
       (fn [this]
         (doall
           (for [child (r/children this)]
             ((:init child) state))))})))

(defn group [key & xs]
  {:key key
   :init (fn [parent-state]
           (let [group-state (r/cursor parent-state [key])]
             (doall (for [x xs] ((:init x) group-state)))))
   :render
   (fn [parent-state]
     (let [group-state (r/cursor parent-state [key])]
       ^{:key (gensym)}
       [:div.group
        {:class (name key)}
        (doall
          (for [x xs]
            ((:render x) group-state)))]))})

(defn text-input [key value]
  {:key key
   :init #(swap! % assoc key value)
   :render
   (fn [parent-state]
     (let [state (r/cursor parent-state [key])]
       ^{:key (gensym)}
       [:input {:type "text"
                :default-value @state
                :on-key-up (fn [e] (when (= 13 (.-which e))
                                     (reset! state (.. e -target -value))))
                :on-blur (fn [e] (reset! state (.. e -target -value)))}]))})

(defn text [key value]
  {:key key
   :init #(swap! % assoc key value)
   :render
   (fn [_]
     [:div value])})

(defn enum [key values value]
  (assert (contains? values value))
  {:key key
   :init #(swap! % assoc key value)
   :render
   (fn [parent-state]
     (let [state (r/cursor parent-state [key])]
       ^{:key (gensym)}
       [:select
        {:default-value @state
         :on-change #(reset! state (.. % -target -value))}
        (for [o values]
          [:option {:value o} o])]))})

(defn dynamic-list [key value type]
  {:key key
   :init #(swap! % assoc key value)
   :render
   (fn [parent-state]
     (let [state (r/cursor parent-state [key])]
       [:div.list
        [:input {:type "text"
                 :placeholder "enter a new value"
                 :default-value ""
                 :on-key-up (fn [e] (when (= 13 (.-which e))
                                      (swap! state conj (.. e -target -value))
                                      (.val (js/$ (.-target e)) "")))}]
        (for [[idx v] (map-indexed vector @state)]
          ((:render (type idx v)) state))]))})

(r/render-component [tree {:foo 1}
                     (text-input :t1 "foo")
                     (text-input :t2 "bar")
                     (group :g1
                            (text-input :t1 "foot")
                            (text-input :t2 "barf"))
                     (group :g2
                            (enum :enum #{"one" "two" "three"} "one")
                            (dynamic-list :list [] text-input))]
                    (.getElementById js/document "app"))

