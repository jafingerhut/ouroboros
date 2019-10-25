(ns ouroboros.foo
  (:require [ouroboros.bar :refer [tail-offset]]))

(deftype Vector [cnt tail]
  ICounted
  (-count [this]
    (tail-offset this)))

(deftype Transient [^:mutable cnt ^:mutable tidx]
  ICounted
  (-count [this]
    (tail-offset this)))

(defn make-vector [x y]
  (Vector. x y))

(defn make-transient [x y]
  (Transient. x y))
