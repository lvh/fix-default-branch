(ns io.lvh.emancipate.git-test
  (:require [io.lvh.emancipate.git :as git]
            [clojure.test :as t]
            [io.lvh.emancipate.test-harness :as h]))

(t/deftest remotes-test
  (t/is (=
         #::h{:result [#::git{:name "origin"
                              :url "git@github.com:lvh/test-emancipation.git"
                              :purpose "fetch"}
                       #::git{:name "origin"
                              :url "git@github.com:lvh/test-emancipation.git"
                              :purpose "push"}]
              :calls [#::h{:fn 'io.lvh.emancipate.git/git!
                           :args ["remote" "--verbose"]}]}
         (h/with-test-harness (git/remotes)))))
