(ns mpnews.routes
  (:require [ring.util.response :refer [redirect]]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [simple-time.core :as t]

            
            [mpnews.data :as v]
            [mpnews.database :as db]
            [mpnews.model :as model]))

;; Handler

(defn insert-new-user [user]
  (let [error (v/user-validation user)]
    
    (if (== (count error) 0) 
      (db/insert-user user)
      (string/join "|" error))))
      

(defn insert-new-article [article]
  (let [error (v/article-validation article)]
    (if (== (count error) 0) 
      (db/insert-article article)
      (string/join "|" error))))
  

(defn insert-new-vendor [vendor]
  (let [error (v/vendor-validation vendor)]
    (if (== (count error) 0) 
      (db/insert-vendor vendor)
      (string/join "|" error))))

;; https://github.com/weavejester/compojure/wiki
(defroutes app-routes
  (GET "/" [] 
    (redirect "mpnews.html"))
  
  (GET "/user" []  
    (db/users))
  
  (POST "/user" [login password email]
    (let [pass_hash "some hash"]
      (let [user (model/->User login email password pass_hash "salt")]
        (insert-new-user user))))
  
  (GET "/vendor" [] 
    (db/vendors))
  
  (POST "/vendor" [nameVendor RSS_path] 
    (let [vendor (model/->Vendor nameVendor RSS_path)]
      (let [inserted (insert-new-vendor vendor)]
        (println inserted)
        inserted)))
  
  (GET "/article" [] 
    (db/articles))
  
  (POST "/article" [title description link image_link category_name]
    (let [now (t/format (t/now) "yyyy-MM-dd HH:mm")]
      (let [article (model/->Article title description link image_link now category_name)]
        (println article)
        (insert-new-article article))))
  
  (route/not-found "Not Found"))
  

