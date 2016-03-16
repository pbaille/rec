(ns rec.multitoggle.comp
  (:require [reagent.core :refer [atom] :as r]))

(defn select-item [state elem max-selected on-change]
  (assert ((:xs @state) elem))
  (if ((set (:selected @state)) elem)
    (swap! state assoc :selected (filter (partial not= elem) (:selected @state)))
    (if (> max-selected (count (:selected @state)))
      (swap! state update :selected (partial cons elem))
      (swap! state update :selected #(cons elem (butlast %)))))
  (on-change (:selected @state)))

(defn multitoggle
  [{:keys [title on-change xs max-selected]
    :as opts
    :or {on-change identity title "multitoggle"}}]

  (let [state (atom {:xs (set xs)
                     :selected ()})]
    (fn []
      [:div.multitoggle.btn-group
       [:span.multitoggle-item.delete-button.btn.btn-default [:i.fa.fa-times]]
       [:div.multitoggle-item.btn.btn-default.title title]
       (doall
         (for [x (:xs @state)]
           ^{:key (gensym)}
           [:div.multitoggle-item.btn.btn-default
            {:class (when ((set (:selected @state)) x) "selected")
             :on-click #(select-item state x max-selected on-change)}
            (name x)]))])))


