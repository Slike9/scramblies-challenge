(ns scramblies-challenge.handler
  (:require
    [reitit.ring :as reitit-ring]
    [scramblies-challenge.middleware :refer [middleware]]
    [scramblies-challenge.scramblies :refer :all]
    [hiccup.page :refer [include-js include-css html5]]
    [config.core :refer [env]]))

(def mount-target
  [:div#app
   [:h2 "Welcome to scramblies-challenge"]
   [:p "please wait while Figwheel is waking up ..."]
   [:p "(Check the js console for hints if nothing exсiting happens.)"]])

(defn head []
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport"
           :content "width=device-width, initial-scale=1"}]
   (include-css (if (env :dev) "/css/site.css" "/css/site.min.css"))])

(defn loading-page []
  (html5
    (head)
    [:body {:class "body-container"}
     mount-target
     (include-js "/js/app.js")]))

(defn index-handler
  [_request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (loading-page)})

(defn api-v1-scramble-handler
  [{{:keys [str1 str2]} :params}]
  {:status 200
   :headers {"Content-Type" "application/edn"}
   :body (pr-str {:result (scramble? str1 str2)})})

(def app
  (reitit-ring/ring-handler
    (reitit-ring/router
      [["/" {:get {:handler index-handler}}]
       ["/items"
        ["" {:get {:handler index-handler}}]
        ["/:item-id" {:get {:handler index-handler
                            :parameters {:path {:item-id int?}}}}]]
       ["/about" {:get {:handler index-handler}}]
       ["/api"
        ["/v1"
         ["/scramble" {:get {:handler api-v1-scramble-handler}}]]]])
    (reitit-ring/routes
      (reitit-ring/create-resource-handler {:path "/" :root "/public"})
      (reitit-ring/create-default-handler))
    {:middleware middleware}))
