(ns mpnews.database
  (:require [clojure.java.jdbc :as db]
            [mpnews.data :refer :all]))

(def mysql-db {:subprotocol "mysql"
               :subname "//127.0.0.1:3306/MPNews"
               :user "root"
               :password ""})

(defn my-insert []
  (db/insert! mysql-db :fruit 
    {:name "Apple" :appearance "rosy" :cost 24}
    {:name "Orange" :appearance "round" :cost 49}))
;; ({:generated_key 1} {:generated_key 2})

(defn insert-user [user]
  (println user)
  (db/insert! mysql-db :user
    {:login (:login user) 
     :email (:email user) 
     :pass_hash (:pass-hash user) 
     :pass_salt (:pass-salt user)}))
  

(defn users []
  (db/query mysql-db ["select * from user"]))

;(j/query mysql-db
;  ["select * from fruit where appearance = ?" "rosy"]
;  :row-fn :cost)
; (24)
