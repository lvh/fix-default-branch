(ns io.lvh.fix-default-branch
  (:gen-class)
  (:require
   [io.lvh.fix-default-branch.github :as gh]
   [io.lvh.fix-default-branch.git :as git]
   [taoensso.timbre :as log]))

(require '[io.lvh.fix-default-branch.https]) ;; for side effect

(defn -main
  "Makes the current branch not be master."
  [& args]
  (let [old-branch "master"
        new-branch "trunk"
        push-remotes (filter (comp #{"push"} ::git/purpose) (git/remotes))
        token (gh/get-token!)
        gh-details #::gh{:token token :new-branch new-branch}]
    (log/info "renaming" old-branch "to" new-branch)
    (git/rename-branch! old-branch new-branch)

    (if (some? token)
      ;; we're using push access here as a proxy for write access
      (doseq [gh-remote (gh/github-remotes push-remotes)]
        (log/info "setting default branch for gh repo" (::git/name gh-remote))
        (gh/set-default-branch! (merge gh-remote gh-details)))
      (log/error "could not find github token to set default branches"))

    (doseq [{::git/keys [name]} push-remotes]
      (log/info "pushing branch" new-branch "to remote" name)
      (git/push-branch! name new-branch)
      (log/info "deleting branch" old-branch "from remote" name)
      (git/delete-branch-remotely! name old-branch))))
