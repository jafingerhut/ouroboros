(ns ouroboros.test-cljs
  (:require [cljs.test :as test]
            [ouroboros.foo :as f]
            ouroboros.test-foo))

(def ^:dynamic *results*)

(defmethod test/report [::test/default :end-run-tests]
  [m]
  (assert (nil? *results*))
  (set! *results* m))

;; Function -main-nashorn is called when running tests using a maven
;; command such as this one:
;; mvn -DCLOJURE_VERSION=1.10.1 -Dclojure.version=1.10.1 clean test

;; Function run is called when running tests using a 'lein cljsbuild'
;; command such as this one:
;; lein with-profile +cljs,+1.10 cljsbuild test

;; To run the tests in a namespace from here, it must be :require'd
;; above, and its name must match the regex given to a run-all-tests
;; call.  You may also call run-all-tests with no args to run tests in
;; all namespaces, but even then it must be :require'd above.

(defn ^:export -main-nashorn []
  (set! *print-newline* false)
  (set! *print-fn* js/print)
  (set! *print-err-fn* js/print)
  (binding [*results* nil]
    (println "Running Basic Tests")
    (test/run-all-tests #"ouroboros\.test-.*")
    (pr-str *results*)))

(defn ^:export run []
  (println "Running Basic Tests")
  (test/run-all-tests #"ouroboros\.test-.*")
  )
