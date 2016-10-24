(ns mpnews.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [clojure.java.jdbc  :refer [jdb]]))

(def mysql-db {:subprotocol "mysql"
               :subname "//127.0.0.1:3306/clojure_test"
               :user "clojure_test"
               :password "clojure_test"})

(j/insert! mysql-db :fruit
  {:name "Apple" :appearance "rosy" :cost 24}
  {:name "Orange" :appearance "round" :cost 49})
;; ({:generated_key 1} {:generated_key 2})

(j/query mysql-db
  ["select * from fruit where appearance = ?" "rosy"]
  :row-fn :cost)
;; (24)
