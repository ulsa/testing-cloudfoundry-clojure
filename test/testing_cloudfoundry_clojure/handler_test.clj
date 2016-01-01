(ns testing-cloudfoundry-clojure.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [testing-cloudfoundry-clojure.handler :refer :all]
            [testing-cloudfoundry-clojure.models.migration :as schema]))

(defn once-fixture [f]
  (println "running once-fixture")
  (schema/migrate)
  (f))

(use-fixtures :once once-fixture)

(deftest test-app
  (testing "main route"
    (let [response (application (mock/request :get "/"))]
      (is (= (:status response) 200))
      (is (= (:body response) "Hello World"))))

  (testing "not-found route"
    (let [response (application (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))
