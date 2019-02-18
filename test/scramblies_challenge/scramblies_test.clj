(ns scramblies-challenge.scramblies-test
  (:require [clojure.test :refer :all]
            [scramblies-challenge.scramblies :refer :all]))

(deftest scramble?-test
  (testing
    (is (scramble? "" ""))
    (is (scramble? "rekqodlw" "world"))
    (is (scramble? "cedewaraaossoqqyt" "codewars"))
    (is (not (scramble? "katas" "steak")))))
