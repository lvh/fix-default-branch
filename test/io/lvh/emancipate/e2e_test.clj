(ns io.lvh.emancipate.e2e-test
  (:require
   [io.lvh.emancipate :as e]
   [io.lvh.emancipate.test-harness :as h]
   [clojure.test :as t]))



(t/deftest end-to-end-test
  (t/is (= #::h{:result nil
                :calls
                [#::h{:fn 'io.lvh.emancipate.git/git!
                      :args ["remote" "--verbose"]}

                 ;; TODO: not entirely clear to me why both are read
                 #::h{:fn 'clojure.core/slurp
                      :path "/home/test/.config/gh/hosts.yml"}
                 #::h{:fn 'clojure.core/slurp
                      :path "/home/test/.config/hub"}

                 #::h{:fn 'io.lvh.emancipate.git/git!
                      :args ["branch" "--move" "master" "trunk"]}

                 #::h{:fn 'io.lvh.emancipate.git/git!
                      :args ["push" "origin" "trunk"]}
                 #::h{:fn 'io.lvh.emancipate.git/git!
                      :args ["push" "origin" ":master"]}

                 #::h{:fn 'io.lvh.emancipate.git/git!
                      :args ["push" "origin" "trunk"]}
                 #::h{:fn 'io.lvh.emancipate.git/git!
                      :args ["push" "origin" ":master"]}

                 #::h{:fn 'clj-http.lite.client/request
                      :request
                      {:method :patch
                       :url "https://api.github.com/repos/lvh/test-emancipation"
                       :headers {"Accept" "application/vnd.github.v3+json"
                                 "Authorization" "token abc123"
                                 "Content-Type" "application/json; charset=utf-8"}
                       :body "{\"default_branch\":\"trunk\"}"}}]}
           (h/with-test-harness (e/-main)))))