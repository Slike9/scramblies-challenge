(ns scramblies-challenge.core
  (:require
    [reagent.core :as reagent :refer [atom]]
    [reagent.session :as session]
    [reitit.frontend :as reitit]
    [clerk.core :as clerk]
    [accountant.core :as accountant]
    [ajax.core :as ajax]
    [ajax.edn]))

;; -------------------------
;; Routes

(def router
  (reitit/router
    [["/" :index]
     ["/about" :about]]))

(defn path-for [route]
  (:path (reitit/match-by-name router route)))


;; -------------------------
;; Page components

(defonce str1 (atom ""))
(defonce str2 (atom ""))
(defonce result (atom ""))

(defn request-scramble
  []
  (ajax/GET "/api/v1/scramble" {:params {:str1 @str1, :str2 @str2}
                                :response-format (ajax.edn/edn-response-format)
                                :handler #(reset! result (:result %))}))

(defn str-input [label value]
  [:div {:class "form-group row"}
   [:label {:class "col-sm-2 col-form-label"} label]
   [:div {:class "col-sm-10"}
    [:input {:type "text"
             :class "form-control"
             :value @value
             :on-change #(reset! value (-> % .-target .-value))}]]])

(defn result-input
  []
  [:div {:class "form-group row"}
   [:label {:class "col-sm-2 col-form-label"} "Result"]
   [:div {:class "col-sm-10"}
    [:input {:type "text"
             :class "form-control-plaintext"
             :readOnly true
             :value @result}]
    [:small {:class "form-text text-muted"}
     "Returns true if a portion of str1 characters can be rearranged to match str2, otherwise returns false"]]])

(defn home-page []
  (fn []
    [:span.main
     [:h1 "Scramblies Challenge"]
     [:form
      [str-input "Str 1" str1]
      [str-input "Str 2" str2]
      [result-input]
      [:div {:class "row"}
       [:div {:class "col-sm-10 offset-sm-2"}
        [:button
         {:type "button"
          :class "btn btn-primary"
          :on-click request-scramble}
         "Scramble?"]]]]]))

(defn about-page []
  (fn [] [:span.main
          [:h1 "About scramblies-challenge"]
          [:p "Returns true if a portion of str1 characters can be rearranged to match str2, otherwise returns false."]]))


;; -------------------------
;; Translate routes -> page components

(defn page-for [route]
  (case route
    :index #'home-page
    :about #'about-page))


;; -------------------------
;; Page mounting component

(defn current-page []
  (fn []
    (let [page (:current-page (session/get :route))]
      [:div
       [:header
        [:p [:a {:href (path-for :index)} "Home"] " | "
         [:a {:href (path-for :about)} "About scramblies-challenge"]]]
       [page]
       [:footer]])))

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (clerk/initialize!)
  (accountant/configure-navigation!
    {:nav-handler
     (fn [path]
       (let [match (reitit/match-by-path router path)
             current-page (:name (:data  match))
             route-params (:path-params match)]
         (reagent/after-render clerk/after-render!)
         (session/put! :route {:current-page (page-for current-page)
                               :route-params route-params})
         (clerk/navigate-page! path)
         ))
     :path-exists?
     (fn [path]
       (boolean (reitit/match-by-path router path)))})
  (accountant/dispatch-current!)
  (mount-root))
