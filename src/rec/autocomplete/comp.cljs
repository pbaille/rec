(ns rec.autocomplete.comp
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [reagent.core :as r :refer [atom]]))

(enable-console-print!)

(defn- is-substring-of [sub main]
  (not= -1 (.indexOf main sub)))

(def lower-case clojure.string/lower-case)

(defn- do-propositions [state]
  (filter #(is-substring-of (:value state) (lower-case %))
          (:data state)))

(defn- on-change [state str]
  (let [data (:data @state)
        typed-so-far (lower-case str)]
    (swap! state
           assoc
           :propositions
           (filter #(is-substring-of typed-so-far (lower-case %))
                   data)
           :value
           typed-so-far)))

(defn- format-data
  "TODO when data passed to autocomplete is an hashmap, group items"
  [raw-data]
  ())

(defn key-handler [state propositions e]
  (let [stay-in-bounds #(mod % (count @propositions))]
    (case (.-which e)
      40 (swap! state update :highlighted (comp stay-in-bounds inc))
      38 (swap! state update :highlighted (comp stay-in-bounds dec))
      13 (let [selected (nth @propositions (:highlighted @state))]
           (.blur (.-target e))
           ((:on-select @state) state selected)
           (swap! state assoc :value selected))
      nil)))

;; stolen from re-com
(defn show-selected-item
  [node]
  (let [item-offset-top (.-offsetTop node)
        item-offset-bottom (+ item-offset-top (.-clientHeight node))
        parent (.-parentNode node)
        parent-height (.-clientHeight parent)
        parent-visible-top (.-scrollTop parent)
        parent-visible-bottom (+ parent-visible-top parent-height)
        new-scroll-top (cond
                         (> item-offset-bottom parent-visible-bottom) (max (- item-offset-bottom parent-height) 0)
                         (< item-offset-top parent-visible-top) item-offset-top)]
    (when new-scroll-top (set! (.-scrollTop parent) new-scroll-top))))

(defn autocomplete
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
        state (atom {:data (apply sorted-set data)
                     :value value
                     :focus? false
                     :highlighted 0
                     :class uniq-class
                     :on-select on-select})
        propositions (reaction (do-propositions @state))
        set-value! #(swap! state assoc :value % :highlighted 0)
        set-focus! #(swap! state assoc :focus? %)
        on-select! (juxt (partial on-select state) set-value!)]
    (r/create-class
      {:reagent-render
       (fn []
         (let [highlighted (:highlighted @state)]
           [:div.autocomplete-container
            {:class uniq-class}
            [:input {:on-key-down (partial key-handler state propositions)
                     :type "text"
                     :placeholder placeholder
                     :on-change #(set-value! (.. % -target -value))
                     :on-focus #(set-focus! true)
                     :on-blur #(set-focus! false)
                     :value (:value @state)}]
            (when (:focus? @state)
              [:div.propositions
               {:style {:max-height (:max-height options :100px)
                        :overflow-y :auto}}
               (doall
                 (for [[p i] (map vector @propositions (range))]
                   ^{:key (gensym)}
                   [:div.proposition
                    {:class (when (= i highlighted) "highlighted")
                     :on-mouse-down #(on-select! p)}
                    p]))])]))
       :component-did-update
       (fn [x]
         (when-let [node (aget (.getElementsByClassName js/document "proposition" (r/dom-node x))
                               (:highlighted @state))]
           (show-selected-item node)))})))