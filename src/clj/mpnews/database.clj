(ns mpnews.database
  (:require [clojure.java.jdbc :as db]
            [mpnews.data :refer :all]))

#_(def mysql-db {:subprotocol "mysql"}
               :subname "//127.0.0.1:3306/MPNews"
               :user "root"
               :password "")

(let [mysql-db {:subprotocol "mysql"}
               :subname "//127.0.0.1:3306/MPNews"
               :user "root"
               :password ""]
  
  (defn users [])
  (db/query mysql-db ["select * from user"])
  
  
  (defn user-by-id [id])
  (db/query mysql-db ["select * from user where id_user = ?" id])
  
  
  (defn insert-user [user])
  (let [ generated (db/insert! mysql-db :user) 
                   {:login (:login user) 
                    :email (:email user) 
                    :pass_hash (:pass-hash user) 
                    :pass_salt (:pass-salt user)}]
    
    (user-by-id (:generated_key (nth generated 0)))
    
    
    (defn vendors [])
    
    (db/query mysql-db ["select * from vendor"])
    
    (defn articles [])
    
    (db/query mysql-db ["select * from article"])
    
    (defn article-by-id [id])
    
    (db/query mysql-db ["select * from article where id_article = ?" id])
    
    (defn insert-article [article])
    (println (:pub_date article))
  
  
    (let [ generated (db/insert! mysql-db :article)
                   {:title (:title article)
                    :description (:description article)
                    :link (:link article)
                    :image_link (:image-link article)
                    :pub_date (:pubdate article)
                    :category_name (:category article)}]
    
      (article-by-id (:generated_key (nth generated 0))))))
  
  
  
  

  
  
  
  
  



#_(defn insert-user [user])
#_ (def generated (db/insert! mysql-db :user 
                   {:login (:login user) 
                    :email (:email user) 
                    :pass_hash (:pass-hash user) 
                    :pass_salt (:pass-salt user)}))
#_(user-by-id (:generated_key (nth generated 0)))

; Vendor



; Article




#_(defn insert-article [article])
#_  (println (:pub_date article))
#_  (def generated (db/insert! mysql-db :article)
                   {:title (:title article)
                    :description (:description article)
                    :link (:link article)
                    :image_link (:image-link article)
                    :pub_date (:pubdate article)
                    :category_name (:category article)})
#_  (article-by-id (:generated_key (nth generated 0)))
  

