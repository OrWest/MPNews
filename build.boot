(set-env!
  :source-paths #{"src/clj" "src/cljs"}
  :resource-paths #{"resources"}
  :dependencies '[[adzerk/boot-cljs "1.7.228-2" :scope "test"]
                  [adzerk/boot-reload "0.4.13" :scope "test"]
                  [adzerk/boot-cljs-repl "0.3.3" :scope "test"]
                  ; for boot-cljs-repl
                  [com.cemerick/piggieback "0.2.1" :scope "test"]
                  [weasel "0.7.0" :scope "test"]
                  [org.clojure/tools.nrepl "0.2.12" :scope "test"]
                  ; project deps
                  [org.clojure/clojure "1.8.0"]
                  [org.clojure/clojurescript "1.9.227"]
                  [reagent "0.6.0-rc"]
                  [ring "1.4.0"]
                  [compojure "1.5.1"]])

(task-options!
  pom {:project 'mpnews
       :version "1.0.0-SNAPSHOT"
       :description "FIXME: write description"}
  aot {:namespace #{'mpnews.core}}
  jar {:main 'mpnews.core})

(require
  '[adzerk.boot-cljs :refer [cljs]]
  '[adzerk.boot-reload :refer [reload]]
  '[adzerk.boot-cljs-repl :refer [cljs-repl]]
  'mpnews.core)

(deftask run []
  (comp
    (watch)
    (reload :asset-path "public"
            :on-jsload 'mpnews.core/init)
    (cljs-repl)
    (cljs :source-map true :optimizations :none)
    (target)
    (with-pre-wrap fileset
      (mpnews.core/-main)
      fileset)))

(deftask build []
  (comp
    (cljs :optimizations :advanced)
    (aot)
    (pom)
    (uber)
    (jar)
    (target)))

