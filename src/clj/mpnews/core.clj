(ns mpnews.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.file :refer [wrap-file]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.util.response :refer [redirect]]
            [clojure.java.io :as io]
            
            [compojure.core :refer :all]
            [compojure.route :as route]
            [simple-time.core :as t]
            
            [mpnews.database :as db])
  (:import [mpnews.data User Vendor Article])
  (:gen-class))

(defonce web-server (atom nil))

(defn start-web-server! [handler]
  (reset! web-server (run-jetty handler {:port 3000 :join? false})))
  
;; routes handle

(defn insert-new-user [login password email]
  (let [user (User. login email password "salt")]
    (db/insert-user user)))
  

(defn insert-new-article [title decription link image-link category]
  (let [now (t/format (t/now) "yyyy-MM-dd HH:mm")]
    (let [article (Article. title decription link image-link now category)]
      (db/insert-article article))))
  
  

(defn insert-new-vendor [name RSS_path]
  (let [vendor (Vendor. name RSS_path)]
    (db/insert-vendor vendor)))
  

;; https://github.com/weavejester/compojure/wiki
(defroutes app-routes
  (GET "/" [] (redirect "mpnews.html"))
  (GET "/user" []  (db/users))
  (POST "/user" [login password email] (insert-new-user login password email))
  (GET "/vendor" [] (db/vendors))
  (POST "/vendor" [nameVendor RSS_path] (insert-new-vendor nameVendor RSS_path))
  (GET "/article" [] (db/articles))
  (POST "/article" [title description link image_link category_name]
    (insert-new-article title description link image_link category_name))
  (route/not-found "Not Found"))

; ----------

(def app (wrap-params app-routes))

(defn dev-main []
  (when-not @web-server
    (.mkdirs (io/file "target" "public"))
    (start-web-server! (wrap-file app "target/public"))))

(defn -main [& args]
  (start-web-server! (wrap-resource app "public")))
  
