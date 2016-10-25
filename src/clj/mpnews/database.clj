(ns mpnews.database
  (:require [clojure.java.jdbc :as db]))

(def mysql-db {:subprotocol "mysql"
               :subname "//127.0.0.1:3306/MPNews"
               :user "root"
               :password ""})

(defn my-insert []
  (db/insert! mysql-db :fruit 
    {:name "Apple" :appearance "rosy" :cost 24}
    {:name "Orange" :appearance "round" :cost 49}))
;; ({:generated_key 1} {:generated_key 2})


;(j/query mysql-db
;  ["select * from fruit where appearance = ?" "rosy"]
;  :row-fn :cost)
; (24)
