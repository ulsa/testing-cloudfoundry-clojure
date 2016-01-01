(ns testing-cloudfoundry-clojure.models.shout
  (:require [clojure.java.jdbc :as sql]
            [environ.core :refer [env]]
            [cheshire.core :as json]))

(defn parse-elephantsql
  "{
 elephantsql: [
               {
                name:        \"postgresql\",
                label:       \"elephantsql\",
                plan:        \"turtle\",
                credentials: {
                              uri: \"postgres://seilbmbd:PHxTPJSbkcDakfK4cYwXHiIX9Q8p5Bxn@babar.elephantsql.com:5432/seilbmbd\"
                              }
                }
               ]
 }"
  [m]
  (get-in m [:elephantsql 0 :credentials :uri]))

(defn- sanitize-uri [uri]
  (clojure.string/replace uri #":[^:]+@" ":******@"))

(defonce spec (if-let [database-url (env :database-url)]
                (do (println "DATABASE_URL is set:" (sanitize-uri database-url))
                    database-url)
                (if-let [vcap-services (env :vcap-services)]
                  (do (println "VCAP_SERVICES is set:" (sanitize-uri vcap-services))
                      (parse-elephantsql (json/parse-string vcap-services true)))
                  (let [default "postgresql://localhost:5432/shouter"]
                    (do (println "Neither DATABASE_URL nor VCAP_SERVICES set, using default:" (sanitize-uri default))
                        default)))))

(defn all []
  (into [] (sql/query spec ["select * from shouts order by id desc"])))

(defn create [shout]
  (sql/insert! spec :shouts [:body] [shout]))