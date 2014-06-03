(ns commitmeter.github
  (:require
    [clj-http.client :as client]
    [cheshire.core :as json]
    [commitmeter.secret :as secret]

    ))

(def api-url "https://api.github.com")
(def token-header {:autorization (str "token " token)})

(defn api [end-point]
  (str api-url end-point))

(defn get-org [name]
  (let
    [uri (str "/orgs/" name)]
    (json/parse-string (:body (client/get (api uri) {:headers token-header})))
    ))

(defn get-repos [org]
  (let
    [uri (org "repos_url")]
    (-> uri
        (client/get {:header token-header})
        :body
        json/parse-string)))
