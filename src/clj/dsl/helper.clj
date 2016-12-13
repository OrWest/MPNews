(ns dsl.helper)

(defrecord Sql [sql args])

(defn- quote-name
  [s]
  (let [x (name s)]
    (if (= "*" x)
      x
      (str \` x \`))))

(defn- join-sqls
  ([] (Sql. "" nil))
  ([^Sql s1 ^Sql s2] (Sql. (str (.sql s1) " " (.sql s2)) (concat (.args s1) (.args s2)))))

(defprotocol SqlLike
  (as-sql [this]))

(extend-protocol SqlLike

  Sql
  (as-sql [this] this)

  Object
  (as-sql [this] (Sql. "?" [this]))

  clojure.lang.Keyword
  (as-sql [this] (Sql. (quote-name this) nil))

  clojure.lang.Symbol
  (as-sql [this] (Sql. (name this) nil))
  
  clojure.lang.ARef
  (as-sql [this] (as-sql @this))
  
  clojure.lang.Sequential
  (as-sql [this] (reduce join-sqls (map as-sql this)))

  nil
  (as-sql [this] (Sql. "NULL" nil)))

(defn to-sql-params
  [relation]
  (let [{s :sql p :args} (as-sql relation)]
    (vec (cons s p))))
