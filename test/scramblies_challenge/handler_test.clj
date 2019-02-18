(ns scramblies-challenge.handler-test
  (:require [clojure.test :refer :all]
            [clojure.edn :as edn]
            [scramblies-challenge.handler :refer [app]]
            [ring.mock.request :as mock]))

(deftest app-test
  (testing "GET /api/v1/scramble"
    (let [response (app (mock/request :get "/api/v1/scramble?str1=rekqodlw&str2=world"))]
      (is (= 200 (:status response)))
      (is (= {:result true} (edn/read-string (:body response)))))
    (let [response (app (mock/request :get "/api/v1/scramble?str1=katas&str2=steak"))]
      (is (= 200 (:status response)))
      (is (= {:result false} (edn/read-string (:body response)))))))
