(ns mpnews.data)
   
; Helper

(defn empty-validation [value]
  (let [pattern #"^\S+$"] 
    (not (nil? (re-find pattern value)))))

(defn email-validation [email]
  (let [pattern #"^.+@.+\..+$"] 
    (not (nil? (re-find pattern email)))))

(defn link-validation [link] 
  (let [pattern #"^(https?:\/\/)?([\da-z\.-]+)\.([a-z\.]{2,6})([\/\w \.-]*)*\/?$"] 
    (not (nil? (re-find pattern link)))))

(defn password-validation [pass]
  (let [pattern #"^.{6,18}$"] 
    (not (nil? (re-find pattern pass)))))

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
                       [#(email-validation (:email %)) "E-mail имеет некорректное значение."]])

(def article-validations [
                          [#(empty-validation (:title %)) "Введите категории"]
                          [#(empty-validation (:decription %)) "Введите описание новости"]
                          [#(link-validation (:link %)) "Введите корректный URL-адрес новости"]
                          [#(link-validation (:image-link %)) "Введите корректный URL-адрес к изображению"]
                          [#(empty-validation (:category %)) "Введите заголовок новости"]])
(def vendor-validations [
                         [#(empty-validation (:name %)) "Введите значение вендора"]
                         [#(link-validation (:RSS_path %)) "Введите корректный RSS адрес"]])


(defn user-validation [user]
  (validate user user-validations))
                                                
(defn article-validation [article]
  (validate article article-validations))
  
(defn vendor-validation [vendor]
  (validate vendor vendor-validations))
