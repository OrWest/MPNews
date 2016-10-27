(ns mpnews.database
  (:require [clojure.java.jdbc :as db]
            [mpnews.data :refer :all]))

(def mysql-db {:subprotocol "mysql"
               :subname "//127.0.0.1:3306/MPNews"
               :user "root"
               :password ""})

(defn users []
  (db/query mysql-db ["select * from user"]))

(defn user-by-id [id]
  (db/query mysql-db ["select * from user where id_user = ?" id]))

(defn insert-user [user]
  (def generated (db/insert! mysql-db :user 
                   {:login (:login user) 
                    :email (:email user) 
                    :pass_hash (:pass-hash user) 
                    :pass_salt (:pass-salt user)}))
  (user-by-id (:generated_key (nth generated 0))))
