(ns io.lvh.fix-default-branch.git-test
  (:require [io.lvh.fix-default-branch.git :as git]
            [clojure.test :as t]
            [io.lvh.fix-default-branch.test-harness :as h]))

(t/deftest remotes-test
  (t/is (=
         #::h{:result [#::git{:name "origin"
                              :url "git@github.com:lvh/test-default-branch-fixing.git"
                              :purpose "fetch"}
                       #::git{:name "origin"
                              :url "git@github.com:lvh/test-default-branch-fixing.git"
                              :purpose "push"}]
              :calls [#::h{:fn 'io.lvh.fix-default-branch.git/git!
                           :args ["remote" "--verbose"]}]}
         (h/with-test-harness (git/remotes)))))
