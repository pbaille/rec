(ns rec.locationpicker.style)

(def styles
  [:.locationpicker
   {:width :500px
    :margin :10px}
   [:.location-map {:width :100% :height :400px}]
   [:.btn-group
    {:margin-bottom :10px}
    [:input.location-radius {:width :100px}]
    [:input.location-text {:width :400px}]]])