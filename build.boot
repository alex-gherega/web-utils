(set-env!
 :source-paths    #{"src/cljs/web_utils"}
 :resource-paths  #{"resources"}
 :dependencies '[[adzerk/boot-cljs          "1.7.228-2"  :scope "test"]
                 [adzerk/boot-cljs-repl     "0.3.3"      :scope "test"]
                 [adzerk/boot-reload        "0.4.13"     :scope "test"]
                 [adzerk/bootlaces          "0.1.13"     :scope "test"]
                 [pandeiro/boot-http        "0.7.6"      :scope "test"]
                 [com.cemerick/piggieback   "0.2.1"      :scope "test"]
                 [org.clojure/tools.nrepl   "0.2.12"     :scope "test"]
                 [weasel                    "0.7.0"      :scope "test"]
                 [org.clojure/clojurescript "1.9.293"]
                 [crisptrutski/boot-cljs-test "0.3.0" :scope "test"]
                 [reagent "0.6.0"]])

;(set-env! :repositories [["clojars" {:url "https://clojars.org/repo/"}]])

(require
 '[adzerk.boot-cljs      :refer [cljs]]
 '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
 '[adzerk.boot-reload    :refer [reload]]
 '[pandeiro.boot-http    :refer [serve]]
 '[crisptrutski.boot-cljs-test :refer [test-cljs]]
 '[boot.git :refer [last-commit]]
 '[adzerk.bootlaces :refer :all]
 )

(def +version+ "0.0.2-SNAPSHOT")
(bootlaces! +version+ :dont-modify-paths? true :ensure-clean false)

(task-options!
;;  push {:repo           "https://clojars.org/repo"
;;        :ensure-branch  "master"
;;        :ensure-clean   true
;;        :ensure-tag     (last-commit)
;;        :ensure-version +version+}
pom  {:project        'web-utils
       :version        +version+
       :description    ""
       :url            "https://github.com/alex-gherega/web-utils"
       :scm            {:url "https://github.com/alex-gherega/web-utils"}
       :license        {"Eclipse Public License" "http://www.eclipse.org/legal/epl-v10.html"}})

(deftask build []
  (comp (speak)
        
        (cljs)
        ))

(deftask run []
  (comp (serve)
        (watch)
        (cljs-repl)
        
        (reload)
        (build)))

(deftask production []
  (task-options! cljs {:optimizations :advanced})
  identity)

(deftask development []
  (task-options! cljs {:optimizations :none}
                 reload {:on-jsload 'app/init})
  identity)

(deftask dev
  "Simple alias to run application in development mode"
  []
  (comp (development)
        (run)))


(deftask testing []
  (set-env! :source-paths #(conj % "test/cljs"))
  identity)

;;; This prevents a name collision WARNING between the test task and
;;; clojure.core/test, a function that nobody really uses or cares
;;; about.
(ns-unmap 'boot.user 'test)

(deftask test []
  (comp (testing)
        (test-cljs :js-env :phantom
                   :exit?  true)))

(deftask auto-test []
  (comp (testing)
        (watch)
        (test-cljs :js-env :phantom)))
