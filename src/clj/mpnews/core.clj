(ns mpnews.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.file :refer [wrap-file]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.params :refer [wrap-params]]
           
            [clojure.java.io :as io]
            
            [mpnews.log :as log]
            [mpnews.routes :as routes])
  (:gen-class))

(defonce web-server (atom nil))

(defn start-web-server! [handler]
  (reset! web-server (run-jetty handler {:port 3000 :join? false})))
  
; ----------

(def app (wrap-params routes/app-routes))

(defn dev-main []
  (when-not @web-server
    (.mkdirs (io/file "target" "public"))
    (start-web-server! (wrap-file app "target/public"))))

(defn -main [& args]
  (start-web-server! (wrap-resource app "public"))
  (log/log "core" "Server has been launched."))
  
