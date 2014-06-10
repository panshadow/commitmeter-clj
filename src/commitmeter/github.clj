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

(defn json-body [resp]
  (if (= (:status resp) 200)
    (-> resp :body (json/parse-string true))
    '()))


(defn urlc
  ([url-tpl] (urlc url-tpl {}))
  ([url-tpl params]
  (let
    [pttrn (re-pattern "\\{\\/([^\\}]+)\\}")
    replacer (fn [item]
                (let
                  [param (params (second item))]
                  (if (nil? param)
                    ""
                    (str "/" param)
                    )))]
    (clojure.string/replace url-tpl pttrn replacer))))
;------------------------------------


(defn get-org [name]
  (let
    [org-url (api (str "/orgs/" name))]
    (-> org-url
        (client/get {:headers token-header})
        (json-body))))

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
      [resp (client/get link {:headers token-header})
       next-url (get-in resp [:links :next :href])
       page (concat items (json-body resp))]

      (if (nil?  next-url)
        page
        (recur next-url page)))))


(defn lazy-fetch-pages [url]
  (let
    [resp (client/get url {:headers token-header})
     next-link (get-in resp [:links :next :href])
     items (json-body resp)]

    (concat
      items
      (if (nil? next-link)
        next-link
        (lazy-seq (lazy-fetch-pages next-link))))))
