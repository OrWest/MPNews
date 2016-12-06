(ns mpnews.log
  (:import (java.io FileWriter)))

(def logAgent (agent (FileWriter. "log.txt" true)))

(defn- extractServerWSPort [channel]
  (.substring (str channel) (+ (.lastIndexOf (str channel) ":") 1)))

(defn- logMessage [out msg]
  (.write out (str msg "\n"))
  (.flush out)
  (println msg)
  out)

(defn log
  ([prefix message]
   (let [datetime (java.util.Date.)
         msg (str "[" datetime "] " prefix " with message " message)]
     (clojure.core/send-off logAgent logMessage msg))))

