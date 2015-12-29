(ns testing-cloudfoundry-clojure.models.shout
  (:require [clojure.java.jdbc :as sql]
            [environ.core :refer [env]]))

(def spec (or (env :database-url)
              "postgresql://localhost:5432/shouter"))

(defn all []
  (into [] (sql/query spec ["select * from shouts order by id desc"])))

(defn create [shout]
  (sql/insert! spec :shouts [:body] [shout]))