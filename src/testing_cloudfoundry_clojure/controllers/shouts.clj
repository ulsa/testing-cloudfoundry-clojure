(ns testing-cloudfoundry-clojure.controllers.shouts
  (:require [compojure.core :refer [defroutes GET POST]]
            [clojure.string :as str]
            [ring.util.response :as ring]
            [testing-cloudfoundry-clojure.views.shouts :as view]
            [testing-cloudfoundry-clojure.models.shout :as model]))

(defn index []
  (view/index (model/all)))

(defn create
  [shout]
  (when-not (str/blank? shout)
    (model/create shout))
  (ring/redirect "/"))

(defroutes routes
           (GET  "/" [] (index))
           (POST "/" [shout] (create shout)))