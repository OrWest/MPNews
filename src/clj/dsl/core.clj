(ns dsl.core
  (:use [dsl.helper]
        [dsl.render]
        [clojure.template])

  (:require [clojure.java.jdbc :as jdbc]
            [mpnews.log :as logger]))


(defn fetch-all [db relation]
  (let [request (to-sql-params relation)]
     (logger/log "DSL" request)
     (jdbc/query db request :result-set-fn vec)))

(defn fetch-one [db relation]
  (first (fetch-all db relation)))

;---- WHERE(EXPRESSION) -------

(defn where* [query expr]
  (assoc query :where (conj-expression (:where query) expr)))

(defn- canonize-operator-symbol
  [op]
  (get '{not= <>, == =} op op))

(defn prepare-expression [e]
  (if (seq? e)
    `(vector
       (quote ~(canonize-operator-symbol (first e)))
       ~@(map prepare-expression (rest e)))
    e))

(defmacro where
  [q body]
  `(where* ~q ~(prepare-expression body)))

;----- JOINS ------

(defn join* [{:keys [tables joins] :as q} type alias table on]
  (let [a (or alias table)]
    (assoc q
      :tables (assoc tables a table)
      :joins (conj (or joins []) [a type on]))))

; этот код разворачивается в 5 объявлений макросов
(do-template [join-name join-key] 

  ; сам шаблон
  (defmacro join-name
    ([relation alias table cond]
     `(join* ~relation ~join-key ~alias ~table ~(prepare-expression cond)))
    ([relation table cond]
     `(let [table# ~table]
        (join* ~relation ~join-key nil table# ~(prepare-expression cond)))))

  ; значения для параметров
  join-inner :inner,
  join :inner,
  join-right :right,
  join-left :left,
  join-full :full)

;----- SIMPLE ------
 
(defn limit [relation v]
  (assoc relation :limit v))

(defn fields [query fd]
  (assoc query :fields fd))

(defn from
  ([q table] (join* q nil table table nil))
  ([q table alias] (join* q nil table alias nil)))

;---- MAIN -------

(defmacro select
  [& body]
  `(-> (map->Select {}) ~@body))

(defrecord Select [fields where order joins tables offet limit]
  SqlLike
  (as-sql [this] (as-sql (render-select this))))
