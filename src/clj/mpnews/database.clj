(ns mpnews.database
  (:require [clojure.java.jdbc :as db]
            [mpnews.log :as log]))

(def mysql-db {:subprotocol "mysql"
               :subname "//127.0.0.1:3306/MPNews"
               :user "root"
               :password ""})

; Basic

(defn get-objects [objectKey]
  (let [request (str "select * from " (name objectKey))]
    (log/log "database" (str "query: \"" request "\""))
    (db/query mysql-db [request])))

(defn get-object-by-id [objectKey id]
  (db/query mysql-db [(str "select * from " (name objectKey) " where id_" (name objectKey) " = ?") id]))

(defn insert-object [objectKey object]
   (let [generated (db/insert! mysql-db objectKey object)]
    (get-object-by-id (name objectKey) (:generated_key (nth generated 0)))))

; User

(defn users []
  (get-objects :user))

(defn user-by-id [id]
  (get-object-by-id :user id))

(defn insert-user [user]
  (let [clear-user (dissoc user :password)]
    (insert-object :user clear-user)))
  

; Vendor

(defn vendors []
  (get-objects :vendor))

(defn vendor-by-id [id]
  (get-object-by-id :vendor id))


(defn insert-vendor [vendor]
  (insert-object :vendor vendor))



; Article

(defn articles []
    (get-objects :article))

(defn article-by-id [id]
    (get-object-by-id :article id))

(defn insert-article [article]
  (insert-object :article article))
  

