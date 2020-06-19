(ns io.lvh.emancipate.https
  (:import (java.lang.reflect Field Modifier)))

(def ^:private modifiers-field
  (doto (.getDeclaredField Field "modifiers")
    (.setAccessible true)))

(defn allow-patch!
  []
  (.setAccessible modifiers-field true)
  (let [methods-field (doto (.getDeclaredField java.net.HttpURLConnection "methods")
                        (.setAccessible true))
        not-final (bit-and-not (.getModifiers methods-field) Modifier/FINAL)]
    (.setInt modifiers-field methods-field not-final)
    (assert (not (-> methods-field .getModifiers Modifier/isFinal)))

    ;; Note, the order of operations here matters a lot! If you .get access
    ;; methods-field while it's still static final, the access result will be
    ;; cached, and this will fail even though ostensibly you disabled final.
    (let [new-methods (-> (.get methods-field nil) seq (conj "PATCH"))]
      (.set methods-field nil (into-array String new-methods)))))

(allow-patch!)
