(ns rec.locationpicker.comp
  (:require [reagent.core :refer [atom] :as r]))

(defn locationpicker [{:keys [on-change]}]
  (r/create-class
    {:reagent-render
     (fn []
       [:div.locationpicker
        [:div.btn-group
         [:input.location-text.btn.btn-default {:type "text"}]
         [:input.location-radius.btn.btn-default {:type "number"}]]
        [:div.location-map]])
     :component-did-mount
     (fn [this]
       (.locationpicker (js/$ ".location-map" (r/dom-node this))
                        (clj->js
                          {:enableAutocomplete true
                           :inputBinding
                           {:radiusInput (js/$ "input.location-radius")
                            :locationNameInput (js/$ "input.location-text")}})))}))