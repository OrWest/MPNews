(ns mpnews.core)

(defrecord User
  [login
   firstName
   lastName
   passwordHash])

(defrecord News
  [header
   description
   imageURL
   text
   createdAt])
   
