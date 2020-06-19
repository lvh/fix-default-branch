(ns io.lvh.emancipate
  (:gen-class)
  (:require
   [io.lvh.emancipate.github :as gh]
   [io.lvh.emancipate.git :as git]
   [taoensso.timbre :as log]))

(require '[io.lvh.emancipate.https]) ;; for side effect

(defn -main
  "Makes the current branch not be master."
  [& args]
  (let [old-branch "master"
        new-branch "trunk"
        remotes (git/remotes)
        token (gh/get-token!)
        gh-details #::gh{:token token :new-branch new-branch}]
    (log/info "renaming" old-branch "to" new-branch)
    (git/rename-branch! old-branch new-branch)

    (log/info "found remotes" remotes)
    (doseq [{::git/keys [name]} remotes]
      (log/info "pushing branch" new-branch "to remote" name)
      (git/push-branch! name new-branch)
      (log/info "deleting branch" old-branch "from remote" name)
      (git/delete-branch-remotely! name old-branch))

    (if (some? token)
      (doseq [gh-remote (->> remotes
                             ;; push is a proxy for repo management access and
                             ;; also conveniently dedupes push/fetch remotes
                             (filter (comp #{"push"} ::git/purpose))
                             gh/github-remotes)]
        (log/info "setting default branch for gh repo" (::git/name gh-remote))
        (gh/set-default-branch! (merge gh-remote gh-details)))
      (log/error "could not find github token to set default branches"))))
