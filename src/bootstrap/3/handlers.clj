(ns bootstrap.3.handlers
  (:require [garden core stylesheet]
            [liberator.core]
            [clojure.java [io :as io] [shell :refer [sh]]]))

(defn lessc [in]
  (clojure.java.shell/with-sh-dir
    (io/resource "node_modules")
    (let [{:keys [out err] :as res} (sh ".bin/lessc" "-" :in in)]
      (if (not-empty err)
        (throw (ex-info err {}))
        out))))

(defn- data->less [{:keys [imports variables]}]
  (str
    (->> imports
         (map garden.stylesheet/at-import)
         garden.core/css)
    (->> variables
         (map (fn [[k v]] [(str "@" (name k)) v]))
         (into {})
         garden.core/style)))

(do
  (def handler
    (liberator.core/resource
      {:available-media-types ["text/css"]
       :handle-ok (fn [ctx]
                    (-> (io/resource "less.edn")
                        slurp
                        clojure.edn/read-string
                        (update :variables merge (get-in ctx [:request :params]))
                        data->less
                        lessc))}))

  #_(handler {:request-method :get
              :params {:brand-primary "rgb(179, 97, 42)"
                       :body-bg "rgb(251, 242, 239)"}
              :headers {"accept" "text/css"}}))
