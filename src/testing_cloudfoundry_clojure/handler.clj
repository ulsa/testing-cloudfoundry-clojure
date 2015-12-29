(ns testing-cloudfoundry-clojure.handler
  (:gen-class)
  (:require [compojure.core :refer [defroutes]]
            [compojure.route :as route]
            [ring.adapter.jetty :as ring]
            [environ.core :refer [env]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [testing-cloudfoundry-clojure.controllers.shouts :as shouts]
            [testing-cloudfoundry-clojure.views.layout :as layout]
            [testing-cloudfoundry-clojure.models.migration :as schema]))

(defroutes routes
           shouts/routes
           (route/resources "/")
           (route/not-found (layout/four-oh-four)))

(def application (wrap-defaults routes site-defaults))

(defn start [port]
  (ring/run-jetty application {:port port
                               :join? false}))

(defn -main [& [port]]
  (schema/migrate)
  (let [port (Integer. (or port (env :port) 5000))]
    (start port)))