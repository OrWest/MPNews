(ns mpnews.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.file :refer [wrap-file]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.util.response :refer [redirect]]
            [clojure.java.io :as io]
            
            [compojure.core :refer :all]
            [compojure.route :as route])
  (:gen-class))

(defonce web-server (atom nil))

(defn start-web-server! [handler]
  (reset! web-server (run-jetty handler {:port 3000 :join? false})))

;https://github.com/weavejester/compojure/wiki
(defroutes app-routes
  (GET "/" [] (redirect "index.html"))
  (route/not-found "Not Found"))

(defn dev-main []
  (when-not @web-server
    (.mkdirs (io/file "target" "public"))
    (start-web-server! (wrap-file app-routes "target/public"))))

(defn -main [& args]
  (start-web-server! (wrap-resource app-routes "public")))
  
