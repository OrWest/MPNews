(ns mpnews.routes
  (:require [ring.util.response :refer [redirect]]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [simple-time.core :as t]
            [clojure.string :as string]

            [mpnews.stat :as stat]
            [mpnews.log :as log]
            [mpnews.data :as v]
            [mpnews.database :as db]
            [mpnews.model :as model]
            [mpnews.rss :as rss]
            [digest :as dig]))

;; Handler

(defn insert-new-user [user]
  (let [error (v/user-validation user)]
    
    (if (== (count error) 0) 
      (let [salt (dig/md5 (t/format (t/now)))
            pass-hash (dig/md5 (:password user))
            pass-hash-with-salt (str pass-hash salt)
            new-pass-hash (dig/md5 pass-hash-with-salt)
            updateUser (assoc user :pass_hash new-pass-hash :pass_salt salt)
            result (db/insert-user updateUser)]
           (log/log "routes" "insert-new-user: user inserted")
           result)
      (string/join "|" error))))
      

(defn insert-new-article [article]
  ;(println "insert" article)
  (let [error (v/article-validation article)]
   ;((println "error count:" (count error) "\nerrors:" error)
    (if (== (count error) 0) 
     ;((println "insert in DB")
      (let [result (db/insert-article article)]
        (log/log "routes" "insert-new-user: article inserted")
        result)
      (string/join "|" error))))
  

(defn insert-new-vendor [vendor]
  (let [error (v/vendor-validation vendor)]
    (if (== (count error) 0) 
      (let [result (db/insert-vendor vendor)]
        (log/log "routes" "insert-new-user: vendor inserted")
        result)
      (string/join "|" error))))

;; https://github.com/weavejester/compojure/wiki
(defroutes app-routes
  (GET "/" [] 
    (log/log "routes" "request: GET /")
    (stat/request-to-main)
    (redirect "mpnews.html"))
  
  (GET "/user" [] 
    (log/log "routes" "request: GET /user")
    (string/join "<br>" (db/users)))
    
  
  (POST "/user" [login password email]
    (log/log "routes" "request: POST /user")
    (let [pass_hash "some hash"]
      (let [user (model/->User login email password pass_hash "salt")]
        (insert-new-user user))))
  
  (GET "/vendor" []
    (log/log "routes" "request: GET /vendor")
    (string/join "<br>" (db/vendors))
    (db/vendors))
  
  (POST "/vendor" [nameVendor RSS_path]
    (log/log "routes" "request: POST /vendor")
    (let [vendor (model/->Vendor nameVendor RSS_path)]
      (let [inserted (insert-new-vendor vendor)]
        inserted)))
  
  (GET "/article" [] 
    (log/log "routes" "request: GET /article")
    (string/join "<br>" (db/articles)))
  
  (POST "/article" [title description link image_link category_name]
    (log/log "routes" "request: POST /article")
    (let [now (t/format (t/now) "yyyy-MM-dd HH:mm")]
      (let [article (model/->Article title description link image_link now category_name)]
        (insert-new-article article))))
  
  (POST "/rss" [url]
    (log/log "routes" "request: POST /rss")
    (let [feeds (rss/load-feeds url)
          models (rss/parse-entries-to-articles (:entries feeds))]
      (for [article models]
        (insert-new-article article))
      "Articles loaded."))
  
  (route/not-found "Not Found"))
  

