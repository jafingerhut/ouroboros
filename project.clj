(defproject ouroboros "0.1.0"
  :description "Demonstrate cljs circular dependency that only causes warning"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]]
  :source-paths ["src/main/clojure" "src/main/cljs"]
  :test-paths ["src/test/clojure"]
  :test-selectors {:default (complement :cljs-nashorn)}
  :jvm-opts ^:replace ["-XX:+UseG1GC"
                       "-XX:-OmitStackTraceInFastThrow"]
  :profiles {:dev {:plugins [[lein-cljsbuild "1.1.7"]]}
             :cljs {:dependencies [[org.clojure/clojure "1.10.1"]
                                   ;;[org.clojure/clojurescript "1.10.238"]
                                   [org.clojure/clojurescript "1.10.520"]]}
             :1.9 {:dependencies [[org.clojure/clojure "1.9.0"]]}
             :1.10 {:dependencies [[org.clojure/clojure "1.10.1"]]}
             :master {:dependencies [[org.clojure/clojure "1.11.0-master-SNAPSHOT"]]}}
  :cljsbuild {:builds {:test {:source-paths ["src/main/cljs"
                                             "src/test/cljs"]
                              :compiler {;;:optimizations :none
                                         ;;:optimizations :whitespace
                                         ;;:optimizations :simple
                                         :optimizations :advanced
                                         :output-to "out/test.js"}}}
              :test-commands
              {"node" ["node" "-e"
                       "require(\"./out/test\"); ouroboros.test_cljs.run()"]
               "spidermonkey" ["js52" "-f" "out/test.js"
                               "--execute=ouroboros.test_cljs.run()"]}})
