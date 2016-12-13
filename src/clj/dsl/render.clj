(ns dsl.render
  (:use [dsl.helper]))

(def NONE (->Sql "" nil))

; большинство фукнций реализуется тривиально
(defn render-limit [s]
  (if-let [l (:limit s)]
    ['LIMIT l]
    NONE))

(defn render-fields [s] '*)  ; пока будем возвращать все столбцы

; эти функции реализуем чуть позже
(defn render-where [s] NONE)
(defn render-order [s] NONE)
(defn render-expression [s] NONE)


; вспомогательная функция
(defn render-table
  [[alias table]]
  (if (= alias table)
    ; если алиас и таблица совпадают, то не выводим 'AS'
    table
    [table 'AS alias]))

(defn render-join-type
  [jt]
  (get
    {nil (symbol ",")
     :cross '[CROSS JOIN],
     :left '[LEFT OUTER JOIN],
     :right '[RIGHT OUTER JOIN],
     :inner '[INNER JOIN],
     :full '[FULL JOIN],
      jt jt}))

; некоторые функции довольно сложные
(defn render-from
  [{:keys [tables joins]}]
  ; секции FROM может и не быть!
  (if (not (empty? joins))
    ['FROM
     ; первый джоин
     (let [[a jn] (first joins)
           t (tables a)]
       ; первый джоин должен делаться при помощи `(from ..)`
       (assert (nil? jn))
       (render-table [a t]))
     ; перебираем оставшиеся джоины
     (for [[a jn c] (rest joins)
           :let [t (tables a)]]
       [(render-join-type jn) ; связка JOIN XX или запятая
        (render-table [a t])  ; имя таблицы и алиас
        (if c ['ON (render-expression c)] NONE)])] ; секция 'ON'
        
    NONE))

(defn render-select
  [select]
  ['SELECT
   (mapv
     #(% select)
     [render-fields
      render-from
      render-where
      render-order
      render-limit])])

