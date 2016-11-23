(ns mpnews.database
  (:require [clojure.java.jdbc :as db]
            [mpnews.data :refer :all]))

(def mysql-db {:subprotocol "mysql"
               :subname "//127.0.0.1:3306/MPNews"
               :user "root"
               :password ""})

; User

(defn users []
  (db/query mysql-db ["select * from user"]))

(defn user-by-id [id]
  (db/query mysql-db ["select * from user where id_user = ?" id]))

(defn insert-user [user]
  (let [generated (db/insert! mysql-db :user user)]
    (user-by-id (:generated_key (nth generated 0)))))
  

; Vendor

(defn vendors []
  (db/query mysql-db ["select * from vendor"]))

(defn vendor-by-id [id]
  (db/query mysql-db ["select * from vendor where id_vendor= ?" id]))


(defn insert-vendor [vendor]
  (let [generated (db/insert! mysql-db :vendor vendor)]
    (vendor-by-id (:generated_key (nth generated 0)))))


; Article

(defn articles []
  (db/query mysql-db ["select * from article"]))

(defn article-by-id [id]
  (db/query mysql-db ["select * from article where id_article = ?" id]))

(defn insert-article [article]
  (let [generated (db/insert! mysql-db :article article)]
    (article-by-id (:generated_key (nth generated 0)))))
  
  

