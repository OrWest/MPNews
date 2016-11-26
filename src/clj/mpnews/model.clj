(ns mpnews.model)

(defrecord User
  [login
   email
   password
   pass_hash
   pass_salt])

(defrecord Article
  [title
   description
   link
   image_link
   pub_date
   category_name])

(defrecord Vendor
  [name
   RSS_path])
