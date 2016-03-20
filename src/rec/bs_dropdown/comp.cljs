(ns rec.bs-dropdown.comp
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [reagent.core :as r :refer [atom]]
            [re-com.core :as rc]))

(enable-console-print!)

(defn- is-substring-of [sub main]
  (not= -1 (.indexOf main sub)))

(def lower-case clojure.string/lower-case)

(defn- do-propositions [state]
  (filter #(is-substring-of (:value state) (lower-case (:name %)))
          (:data state)))

(defn- formatted-item? [x]
  (every? identity ((juxt :id :idx :name) x)))

(defn format-data
  "format the data for usage in dropdown
  can take data in two forms
  1:
  {:category1 [items ...] :category2 [items ...]}
  2:
  [items ...]

  => [{:id string :idx int :name string :category (maybe string)} ...]"
  [data]
  (cond
    (map? data)
    (mapcat (fn [[cat items]]
              (map-indexed (fn [idx item] {:id (str (name cat) "_" idx)
                                           :idx idx
                                           :name item
                                           :category cat})
                           items))
            data)
    (every? string? data)
    (map-indexed #(hash-map :id %2 :idx %1 :name %2 :category nil) data)
    (every? formatted-item? data)
    data))



(defn- highlighted-index [highlighted propositions]
  (let [idx (.indexOf (clj->js (map :id propositions)) highlighted)]
    (when-not (= -1 idx) idx)))

(defn- nxt-item [highlighted propositions]
  (let [ps @propositions
        idx (highlighted-index highlighted ps)]
    (if (= idx (dec (count ps)))                            ;is last element
      (first ps)
      (nth ps (inc idx)))))

(defn- prev-item [highlighted propositions]
  (let [ps @propositions
        idx (highlighted-index highlighted ps)]
    (if (zero? idx)                                         ;is first element
      (last ps)
      (nth ps (dec idx)))))

(defn find-by-id [map-seq id]
  (first (filter #(when-let [v (:id %)] (= v id)) map-seq)))

(defn key-handler [state propositions on-select! e]
  (let [highlighted (:highlighted @state)]
    (case (.-which e)
      40 (swap! state assoc :highlighted (:id (nxt-item highlighted propositions)))
      38 (swap! state assoc :highlighted (:id (prev-item highlighted propositions)))
      27 (do (.blur (.-target e)) (swap! state assoc :value ""))
      13 (when-let [selected (find-by-id @propositions (:highlighted @state))]
           (on-select! selected)
           (.blur (.-target e))
           (swap! state assoc :value ""))

      nil)))

;; stolen from re-com
(defn- show-selected-item
  [node]
  (let [item-offset-top (.-offsetTop node)
        item-offset-bottom (+ item-offset-top (.-clientHeight node))
        parent (.-parentNode (.-parentNode node))
        parent-height (.-clientHeight parent)
        parent-visible-top (.-scrollTop parent)
        parent-visible-bottom (+ parent-visible-top parent-height)
        new-scroll-top (cond
                         (> item-offset-bottom parent-visible-bottom) (max (- item-offset-bottom parent-height) 0)
                         (< item-offset-top parent-visible-top) item-offset-top)]
    (when new-scroll-top (set! (.-scrollTop parent) new-scroll-top))))

(defn bs-dropdown
  [{:keys [data value placeholder on-select on-focus on-blur focus]
    :as options
    :or {value ""
         data []
         placeholder "Type something"
         on-focus identity
         on-blur identity
         focus false
         on-select identity}}]
  (let [uniq-class (str (gensym))
        formated (format-data data)
        state (atom {:data formated
                     :value value
                     :focus focus
                     :highlighted (:id (first formated))
                     :class uniq-class
                     :on-select on-select})
        propositions (reaction (do-propositions @state))
        set-value! #(swap! state assoc
                           :value %
                           :highlighted (:id (first (do-propositions (assoc @state :value %)))))
        set-focus! #(do (swap! state assoc :focus %) ((if % on-focus on-blur) state))
        on-select! (juxt (partial on-select state) (comp set-value! :name))]
    (r/create-class
      {:reagent-render
       (fn []
         (let [highlighted (:highlighted @state)]
           [:div.bs-dropdown-container.form-control
            {:class uniq-class
             :style {:padding 0
                     :z-index :auto}}
            [:input
             {:style {:width :100%
                      :border 0
                      :outline :none
                      :padding 0
                      :padding-left :8px
                      :height :100%
                      :background :transparent}
              :on-key-down (partial key-handler state propositions on-select!)
              :type "text"
              :placeholder placeholder
              :on-change #(set-value! (.. % -target -value))
              :on-focus #(set-focus! true)
              :on-blur #(set-focus! false)
              :value (:value @state)
              :auto-focus (:focus @state)}]
            (when (:focus @state)
              [:div.propositions
               {:style {:max-height :120px
                        :overflow-y :auto
                        :z-index 10}}
               (doall
                 (for [[cat items] (group-by :category @propositions)]
                   ^{:key (gensym)}
                   [:div.category
                    (if cat [:div.cat-title (clojure.string/capitalize (name cat))]
                            {:class "unique"})
                    (doall
                      (for [{:keys [name id] :as item} (sort-by :idx items)]
                        ^{:key (gensym)}
                        [:div.proposition
                         {:class (when (= id highlighted) "highlighted")
                          :on-mouse-down #(on-select! item)}
                         name]))]))])]))

       :component-did-update
       (fn [x]
         (when-let [node (aget (.getElementsByClassName js/document "proposition" (r/dom-node x))
                               (.indexOf (clj->js (map :id @propositions)) (:highlighted @state)))]
           (show-selected-item node)))})))

;; tests --------------------------------------------------------------------
