(ns mpnews.stat
  (:require [mpnews.log :as l]
            [clojure.core :as cloj]))

(def request-main-count (atom 0))
(def request-count-to-log 100)

(defn request-to-main [] 
  (swap! request-main-count cloj/inc)
  (if (== (mod @request-main-count request-count-to-log) 0)
    (l/log "request-count" (str @request-count))))
