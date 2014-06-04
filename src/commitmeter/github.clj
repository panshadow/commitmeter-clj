(ns commitmeter.github
  (:require
    [clj-http.client :as client]
    [cheshire.core :as json]
    [commitmeter.secret :as secret]

    ))

(def api-url "https://api.github.com")
(def token-header {:authorization (secret/app-token)})


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
        (client/get {:headers token-header})
        :body
        (json/parse-string true)
        )))

(defn fetch-pages [url]
  (loop [link url items (list)]
    (let
      [resp (client/get url {:headers token-header} :debug)
       next-url (get-in resp [:links :next :href])
       page (-> resp :body (json/parse-string true))
       ]

      (if (nil?  next-url)
        (concat items page)
        (recur next-url (concat items page))))))
