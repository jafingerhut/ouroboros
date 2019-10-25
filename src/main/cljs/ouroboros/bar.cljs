(ns ouroboros.bar)

(defn tail-offset [vec]
  (let [cnt (.-cnt vec)
        _ (if (instance? ouroboros.foo/Transient vec)
            (println "dbg tail-offset type=" (type vec) " val=" true
                     " tidx=" (.-tidx vec))
            (println "dbg tail-offset type=" (type vec) " val=" false
                     " (.-tail vec)=" (.-tail vec)))
        tail-len (if (instance? ouroboros.foo/Transient vec)
                   (.-tidx vec)
                   (.-tail vec))]
    (- cnt tail-len)))
