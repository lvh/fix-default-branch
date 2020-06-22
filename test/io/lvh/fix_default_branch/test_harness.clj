(ns io.lvh.fix-default-branch.test-harness
  (:require
   [io.lvh.fix-default-branch.github :as gh]
   [io.lvh.fix-default-branch.git :as git]
   [clj-http.lite.client :as http]
   [clojure.string :as str]
   [clojure.java.io :as io]
   [taoensso.timbre :as log]))

(def ^:dynamic *calls* (atom []))
;; This probably doesn't to be dynamic since we're using with-redefs anyway,
;; which alters var roots, but I like signaling that it's "local-ish" state.

(def hosts-yml (-> "hosts.yml" io/resource slurp))
(def hub-yml (-> "hub" io/resource slurp))
(def ssh-github-remotes (-> "ssh-github-remotes" io/resource slurp))

(defmacro with-test-harness
  [& body]
  `(with-redefs
     [git/git!
      (fn [& args#]
        (log/info "entering fake git!" args#)
        (swap! *calls* conj {::fn `git/git! ::args args#})
        (log/spy
         (case (log/spy args#)
           ["remote" "--verbose"]
           {:out ~ssh-github-remotes}
           nil)))

      http/request
      (fn [req#]
        (log/info "entering fake request" req#)
        (swap! *calls* conj {::fn `http/request ::request req#}))

      slurp
      (fn [path#]
        (log/info "entering fake slurp" path#)
        (swap! *calls* conj {::fn `slurp ::path path#})
        (condp (fn [part# path#] (str/ends-with? path# part#))
            "hosts.yml" ~hosts-yml
            "hub" ~hub-yml))


      gh/prop
      (fn [prop#]
        (case prop#
          "user.home" "/home/test"))]
     (reset! *calls* [])
     {::result (do ~@body)
      ::calls @*calls*}))
