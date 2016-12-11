(ns mpnews.log
  (:import (java.io FileWriter)))

(def logger (agent (FileWriter. "log.txt" true)))

(defn- logMessage [out msg]
  (.write out (str msg "\n"))
  (.flush out)
  (println msg)
  out)

(defn log [prefix message]
   (let [datetime (java.util.Date.)
         msg (str "[" datetime "][" prefix "]: " message)]
     (clojure.core/send-off logger logMessage msg)))

