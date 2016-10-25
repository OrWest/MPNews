(ns mpnews.date)

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
   
