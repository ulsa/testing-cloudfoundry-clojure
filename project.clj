(defproject testing-cloudfoundry-clojure "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :mirrors {#"clojars" {:name "clojars mirror"
                        :url "https://clojars-mirror.tcrawley.org/repo/"}}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.5.0"]
                 [hiccup "1.0.5"]
                 [ring/ring-defaults "0.2.0"]
                 [ring/ring-jetty-adapter "1.4.0"]
                 [org.clojure/java.jdbc "0.5.0"]
                 [org.postgresql/postgresql "9.4.1208"]
                 [cheshire "5.5.0"]
                 [environ "1.0.2"]
                 [enlive "1.1.6"]]
  :plugins [[lein-ring "0.9.7"]
            [lein-environ "1.0.1"]]
  :ring {:handler testing-cloudfoundry-clojure.handler/application}
  :profiles
  {:dev     {:dependencies [[javax.servlet/servlet-api "2.5"]
                            [ring/ring-mock "0.3.0"]]}
   :uberjar {:uberjar-name "testing-cloudfoundry-clojure-standalone.jar"
             :main         testing-cloudfoundry-clojure.handler
             :aot          :all}})
