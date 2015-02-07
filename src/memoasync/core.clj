(ns memoasync.core
  (:require [clojure.core.async :as a]
            [clojure.core.cache :as c]))

(defn- update-cache [cache key value]
  (if (c/has? cache key)
    (c/hit cache key)
    (c/miss cache key value)))

(defn memo [cache f]
  (let [cache (atom cache)]
    (with-meta
      (fn [& params]
        (a/go
          (when-let [res (or (c/lookup @cache params)
                             (a/<! (apply f params)))]
            (swap! cache update-cache params res)
            res)))
      {::cache cache})))

(defn- make-memo-fn [memo-factory]
  (fn [f & opts]
    (memo (apply memo-factory {} opts) f)))

(def fifo (make-memo-fn c/fifo-cache-factory))
(def lru (make-memo-fn c/lru-cache-factory))
(def ttl (make-memo-fn c/ttl-cache-factory))
(def lu (make-memo-fn c/lu-cache-factory))
(def lirs (make-memo-fn c/lirs-cache-factory))
(def soft (make-memo-fn c/soft-cache-factory))
