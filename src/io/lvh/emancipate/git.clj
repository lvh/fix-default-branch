(ns io.lvh.emancipate.git
  "Tools for modifying local Git repositories."
  (:require [clojure.java.shell :refer [sh]]
            [clojure.string :as str]
            [taoensso.timbre :as log]))

(defn ^:private git!
  [& args]
  (log/debug "running git command: git" args)
  (let [{:keys [exit] :as res} (apply sh "git" args)]
    (if (zero? exit)
      res
      (throw (ex-info "git command failed" {::result res ::args args})))))

(defn rename-branch!
  "Renames a branch."
  [old-name new-name]
  (git! "branch" "--move" old-name new-name))

(defn delete-branch!
  "Deletes a branch (locally.)."
  [branch-name]
  (git! "branch" "--delete" branch-name))

(defn push-branch!
  "Pushes a branch to the given remote."
  [remote branch-name]
  (git! "push" remote branch-name))

(defn delete-branch-remotely!
  "Deletes a branch on the given remote."
  [remote branch-name]
  (push-branch! remote (str ":" branch-name)))

(def ^:private git-remote-re
  #"(?<name>.*?)\s+(?<url>.*?)\s+\((?<purpose>.*?)\)")

(defn remotes
  "List and parse all the remotes."
  []
  (let [{:keys [out]} (git! "remote" "--verbose")]
    (->> out
         (str/split-lines)
         (map
          (fn [line]
            (->> line
                 (re-find git-remote-re)
                 (rest) ;; discard full match, keep parts
                 (zipmap [::name ::url ::purpose])))))))

#_(remotes)
