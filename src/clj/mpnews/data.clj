(ns mpnews.data)

(defrecord User
  [login
   email
   pass-hash
   pass-salt])

(defrecord News
  [header
   description
   imageURL
   text
   createdAt])
   
