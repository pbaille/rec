(ns rec.multi-select.comp
  (:require [reagent.core :as r :refer [atom]]
            [rec.autocomplete.comp :refer [autocomplete]]))



(defn multi-select
  [{:keys [data selected placeholder on-change icon-class]
    :or {selected []
         data []
         placeholder "Type something"
         on-change identity
         icon-class :zmdi-github-alt}}]
  (let [state (atom {:selected (set selected)
                     :data (set data)
                     :focus false})]
    (fn []
      [:div.multiselect-container
       {:class (when (:focus @state) "active")}
       ;; :o
       [:div.select-bar
        [:i {:class (str "zmdi " (name icon-class))}]
        [(autocomplete
           {:data (:data @state)
            :value ""
            :on-focus #(swap! state assoc :focus true)
            :on-blur #(swap! state assoc :focus false)
            :focus (:focus @state)
            :on-select (fn [_ v]
                         (println "here")
                         (reset! state
                                 (-> @state
                                     (update :data disj v)
                                     (update :selected conj v)))
                         (on-change (:selected @state)))})]]
       [:div.selected-container
        (for [s (:selected @state)]
          ^{:key (gensym)}
          [:div.selected-item
           {:on-mouse-down (fn [_]
                             (reset! state
                                     (-> @state
                                         (update :data conj s)
                                         (update :selected disj s)))
                             (on-change (:selected @state)))}
           [:i.zmdi.zmdi-delete] [:span s]])]])))