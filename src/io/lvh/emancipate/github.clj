(ns io.lvh.emancipate.github
  "Tools for talking to GitHub."
  (:require
   [clj-yaml.core :as yaml]
   [cheshire.core :as json]
   [clj-http.lite.client :as http]
   [io.lvh.emancipate.git :as git]))

(defn ^:private prop
  "Get a Java system property."
  [name]
  (System/getProperty name))

(defn ^:private get-config!
  [rel-path]
  (let [path (str (prop "user.home") "/" rel-path)]
    (-> path slurp yaml/parse-string)))

(defn get-gh-token!
  []
  (-> ".config/gh/hosts.yml" get-config! :github.com :oauth_token))

#_(get-gh-token!)

(defn get-hub-token!
  []
  (-> ".config/hub" get-config! :github.com first :oauth_token))

#_(get-hub-token!)

(defn get-token!
  "\"Borrow\" a personal access token from gh/hub."
  []
  (or (get-gh-token!) (get-hub-token!)))

(defn set-default-branch!
  [{::keys [owner repo token new-branch]}]
  (http/request
   {:method :patch
    :url (format "https://api.github.com/repos/%s/%s" owner repo)
    :headers {"Accept" "application/vnd.github.v3+json"
              "Authorization" (str "token " token)
              "Content-Type" "application/json; charset=utf-8"}
    :body (json/generate-string {"default_branch" new-branch})}))

(def ^:private github-ssh-re
  #"git@github.com:(?<owner>[A-Za-z0-9-]*)/(?<repo>[A-Za-z0-9-]*)\.git")

(def ^:private github-https-re
  #"https://github\.com/(?<owner>[A-Za-z0-9-]*)/(?<repo>[A-Za-z0-9-]*)\.git")

(defn github-remotes
  "Given a remotes structure from git, find all the ones that are GitHub remotes
  and parse them."
  [remotes]
  (for [{::git/keys [url] :as remote} remotes
        :let [ssh (re-find github-ssh-re url)
              https (re-find github-https-re url)
              [owner repo :as match] (rest (or ssh https))]
        :when match]
    (assoc remote
           ::type (cond (some? ssh) :ssh (some? https) :https)
           ::owner owner
           ::repo repo)))
