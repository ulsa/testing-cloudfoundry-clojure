(defproject testing-cloudfoundry-clojure "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [compojure "1.4.0"]
                 [hiccup "1.0.5"]
                 [ring/ring-defaults "0.1.5"]
                 [ring/ring-jetty-adapter "1.4.0"]
                 [org.clojure/java.jdbc "0.4.2"]
                 [org.postgresql/postgresql "9.4.1207"]
                 [environ "1.0.1"]]
  :plugins [[lein-ring "0.9.7"]
            [lein-environ "1.0.1"]]
  :ring {:handler testing-cloudfoundry-clojure.handler/app}
  :profiles
  {:dev     {:dependencies [[javax.servlet/servlet-api "2.5"]
                            [ring/ring-mock "0.3.0"]]}
   :uberjar {:uberjar-name "testing-cloudfoundry-clojure-standalone.jar"
             :main         testing-cloudfoundry-clojure.handler
             :aot          :all}})
