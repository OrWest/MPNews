(ns mpnews.data)
   
; Helper

(defn empty-validation [value]
  (if (nil? value)
    false
    (not (empty? value))))

(defn password-validation [pass]
  (if (nil? pass)
    false
    (and (> (count pass) 5) (< (count pass) 18))))

; Validation

(defn red [accum pair obj]
  (let [pred (nth pair 0)]
    (if-not (pred obj)
      (conj accum (nth pair 1)))))

(defn validate [obj rules]
  (reduce #(red %1 %2 obj) [] rules)) 

(def user-validations [
                       [#(empty-validation (:login %)) "Логин не может быть пустым."]
                       [#(password-validation (:password %)) "Пароль должен быть не менее 6 символов и не более 18"]
                       [#(empty-validation (:email %)) "Введите E-mail."]])

(def article-validations [
                          [#(empty-validation (:title %)) "Введите категории"]
                          [#(empty-validation (:description %)) "Введите описание новости"]
                          [#(empty-validation (:link %)) "Введите URL-адрес новости"]
                          [#(empty-validation (:image_link %)) "Введите URL-адрес к изображению"]
                          [#(empty-validation (:category_name %)) "Введите заголовок новости"]])
(def vendor-validations [
                         [#(empty-validation (:name %)) "Введите значение вендора"]
                         [#(empty-validation (:RSS_path %)) "Введите RSS адрес"]])


(defn user-validation [user]
  (validate user user-validations))
                                                
(defn article-validation [article]
  (validate article article-validations))
  
(defn vendor-validation [vendor]
  (validate vendor vendor-validations))
