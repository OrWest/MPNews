(ns mpnews.log
  (:import (java.io FileWriter)))

(def logAgent (agent (FileWriter. "log.txt" true)))

(defn- logMessage [out msg]
  (.write out (str msg "\n"))
  (.flush out)
  (println msg)
  out)

(defn log [prefix message]
   (let [datetime (java.util.Date.)
         msg (str "[" datetime "][" prefix "]: " message)]
     (clojure.core/send-off logAgent logMessage msg)))

