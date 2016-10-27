(ns mpnews.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.file :refer [wrap-file]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.util.response :refer [redirect]]
            [clojure.java.io :as io]
            
            [compojure.core :refer :all]
            [compojure.route :as route]
            
            [mpnews.database :as db])
  (:import [mpnews.data User])
  (:gen-class))

(defonce web-server (atom nil))

(defn start-web-server! [handler]
  (reset! web-server (run-jetty handler {:port 3000 :join? false})))

(defn insert-new-user [login password email]
  (def user (User. login email password "salt"))
  (db/insert-user user))
  

;https://github.com/weavejester/compojure/wiki
(defroutes app-routes
  (GET "/" [] (redirect "mpnews.html"))
  (GET "/user" []  (db/users))
  (POST "/user" [login password email] (insert-new-user login password email))
  (route/not-found "Not Found"))

; ----------

(def app (wrap-params app-routes))

(defn dev-main []
  (when-not @web-server
    (.mkdirs (io/file "target" "public"))
    (start-web-server! (wrap-file app "target/public"))))

(defn -main [& args]
  (start-web-server! (wrap-resource app "public")))
  
