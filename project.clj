(defproject memoasync "0.1.0-SNAPSHOT"
  :description "Memoisation of async operations"
  :url "https://github.com/benashford/redis-async"
  :license {:name         "Apache Licence 2.0"
            :url          "http://www.apache.org/licenses/LICENSE-2.0"
            :distribution :repo}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/core.cache "0.6.4"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]])
