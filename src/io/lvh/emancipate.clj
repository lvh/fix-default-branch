(ns io.lvh.emancipate
  (:gen-class)
  (:require
   [io.lvh.emancipate.github :as gh]
   [io.lvh.emancipate.git :as git]))

(defn -main
  "Makes the current branch not be master."
  [& args]
  (let [old-branch "master"
        new-branch "trunk"
        remotes (git/remotes)
        gh-remotes (gh/github-remotes remotes)
        gh-details #::gh{:token (gh/get-token!) :new-branch new-branch}]
    (git/rename-branch! old-branch new-branch)

    (doseq [gh-remote gh-remotes]
      (gh/set-default-branch! (merge gh-remote gh-details)))

    (doseq [{::git/keys [remote-name]} remotes]
      (git/push-branch! remote-name new-branch)
      (git/delete-branch! remote-name old-branch))))
