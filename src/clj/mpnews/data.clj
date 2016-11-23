(ns mpnews.data)

(defrecord User
  [login
   email
   pass-hash
   pass-salt])

(defrecord Article
  [title
   description
   link
   image-link
   pubdate
   category-name])

(defrecord Vendor
  [name
   RSS_path])
   
