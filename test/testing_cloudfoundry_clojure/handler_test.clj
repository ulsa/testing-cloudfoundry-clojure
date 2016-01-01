(ns testing-cloudfoundry-clojure.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [testing-cloudfoundry-clojure.handler :refer :all]
            [testing-cloudfoundry-clojure.models.migration :as schema]
            [net.cgrand.enlive-html :refer [html-snippet select]]))

(defn once-fixture [f]
  (println "running once-fixture")
  (schema/migrate)
  (f))

(use-fixtures :once once-fixture)

(deftest test-app
  (testing "main route"
    (let [response (application (mock/request :get "/"))
          page (html-snippet (:body response))]
      (is (= (:status response) 200))
      (is (= (->> (select page [:div#header :h1.container])
                  (mapv :content)
                  ffirst) "SHOUTER"))))

  (testing "not-found route"
    (let [response (application (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))
