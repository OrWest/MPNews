(ns dsl.core
  (:use [dsl.helper]
        [dsl.render])
  (:require [clojure.java.jdbc :as jdbc]))


(defn fetch-all [db relation]
  (println (to-sql-params relation))
  (jdbc/query db (to-sql-params relation) :result-set-fn vec))

(defn fetch-one [db relation]
  (first (fetch-all db relation)))


 

(defn limit [relation v]
  (assoc relation :limit v))

(defn fields [query fd]
  (assoc query :fields fd))

(defn where [query expr]
  (assoc query :where (conj-expression (:where query) expr)))

(defn join* [{:keys [tables joins] :as q} type alias table on]
  (let [a (or alias table)]
    (assoc q
      :tables (assoc tables a table)
      :joins (conj (or joins []) [a type on]))))

(defn from
  ([q table] (join* q nil table table nil))
  ([q table alias] (join* q nil table alias nil)))

(defn join-cross
  ([q table] (join* q :cross table table nil))
  ([q table alias] (join* q :cross table alias nil)))

(defmacro select
  [& body]
  `(-> (map->Select {}) ~@body))

(defrecord Select [fields where order joins tables offet limit]
  SqlLike
  (as-sql [this] (as-sql (render-select this))))
