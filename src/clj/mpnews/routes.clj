(ns mpnews.routes
  (:require [ring.util.response :refer [redirect]]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [simple-time.core :as t]
            [clojure.string :as string]

            [mpnews.log :as log]
            [mpnews.data :as v]
            [mpnews.database :as db]
            [mpnews.model :as model]))

;; Handler

(defn insert-new-user [user]
  (let [error (v/user-validation user)]
    
    (if (== (count error) 0) 
      ((db/insert-user user
        (log/log "routes" "insert-new-user: user inserted")))
      (string/join "|" error))))
      

(defn insert-new-article [article]
  (let [error (v/article-validation article)]
    (if (== (count error) 0) 
      ((db/insert-article article)
       (log/log "routes" "insert-new-user: article inserted"))
      (string/join "|" error))))
  

(defn insert-new-vendor [vendor]
  (let [error (v/vendor-validation vendor)]
    (if (== (count error) 0) 
      ((db/insert-vendor vendor)
       (log/log "routes" "insert-new-user: vendor inserted"))
      (string/join "|" error))))

;; https://github.com/weavejester/compojure/wiki
(defroutes app-routes
  (GET "/" [] 
    (log/log "routes" "request: GET /")
    (redirect "mpnews.html"))
  
  (GET "/user" [] 
    (log/log "routes" "request: GET /user")
    (db/users))
  
  (POST "/user" [login password email]
    (log/log "routes" "request: POST /user")
    (let [pass_hash "some hash"]
      (let [user (model/->User login email password pass_hash "salt")]
        (insert-new-user user))))
  
  (GET "/vendor" []
    (log/log "routes" "request: GET /vendor")
    (db/vendors))
  
  (POST "/vendor" [nameVendor RSS_path]
    (log/log "routes" "request: POST /vendor")
    (let [vendor (model/->Vendor nameVendor RSS_path)]
      (let [inserted (insert-new-vendor vendor)]
        inserted)))
  
  (GET "/article" [] 
    (log/log "routes" "request: GET /article")
    (db/articles))
  
  (POST "/article" [title description link image_link category_name]
    (log/log "routes" "request: POST /article")
    (let [now (t/format (t/now) "yyyy-MM-dd HH:mm")]
      (let [article (model/->Article title description link image_link now category_name)]
        (insert-new-article article))))
  
  (route/not-found "Not Found"))
  

