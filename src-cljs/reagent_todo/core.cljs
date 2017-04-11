(ns reagent-todo.core
  (:require [clojure.string :as string]
            [reagent.core :as r]))

(enable-console-print!)

;; The "database" of your client side UI.
(def app-state
  (r/atom
   {:todos
    [{:name "Get something done"}
     {:name "Get something else done"}]}))

(defn display-todo [todo]
  [:p {:class "lead"} (:name todo)
   [:span {:class "pull-right"
           :style {:cursor "pointer"}
           :on-click #(remove-todo! todo)}
    "x"]])

(defn add-todo! [f & args]
  (apply swap! app-state update-in [:todos] f args))

(defn remove-todo! [todo]
  (add-todo! (fn [cs]
                      (vec (remove #(= % todo) cs)))
                    todo))

(defn input-button []
  (let [val (r/atom "")]
    (fn []
      [:div
       [:div {:class "form-group"}
        [:input {:type "text"
                 :placeholder "Do something awesome"
                 :value @val
                 :class "form-control"
                 :on-change #(reset! val (-> % .-target .-value))}]]
       [:button {:on-click #(add-todo! conj {:name @val}) :class "btn btn-success"}
        "Add todo"]])))

(defn todo-app []
  [:div {:class "row"}
   [:h1 "Tooodoo"]
   [:div {:class "col-xs-6"}
    [input-button]]
   [:p (:currentValue @app-state)]
   [:div {:class "col-xs-6"}
    (for [todo (:todos @app-state)]
      [display-todo todo])]])

;; Render the root component
(defn start []
  (r/render-component
   [todo-app]
   (.getElementById js/document "root")))
