(ns mpnews.rss
  (:require [clj-rome.reader :as reader]
            [mpnews.model :as model]))

; https://news.tut.by/rss/index.rss

(defn load-feeds [url] 
  (reader/build-feed url {:lazy? false}))

(defn parse-entries-to-articles [entries]
  (for [feed entries]
    (let [title (:title feed)
          description (:value (:description feed))
          link (:link feed)
          image_link (:url (first (:enclosures feed)))
          pub_date (:published-date feed)
          category_name "category!!!"]
      (model/->Article title description link image_link pub_date category_name))))
      
          
