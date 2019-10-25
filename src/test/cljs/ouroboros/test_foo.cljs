(ns ouroboros.test-foo
  (:require [clojure.test :as test :refer [deftest testing is are]]
            [ouroboros.foo :as f]))

(deftest test-one
  (println "got here #1")
  (let [v (f/make-vector 3 2)
        t (f/make-transient 7 6)]
    (is (= (count v) (count t))))
  (println "got here #2"))
