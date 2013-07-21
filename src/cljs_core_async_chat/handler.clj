(ns cljs-core-async-chat.handler
  (:use
   compojure.core)
  (:require
   [clojure.edn :as edn]
   [org.httpkit.server :as httpkit]
   [cljs-core-async.middleware :refer [wrap-timbre]]
   [compojure.handler :as handler]
   [compojure.route :as route]
   [clj-time.local :as lt]
   [taoensso.timbre :as timbre
    :refer (trace debug info warn error fatal spy with-log-level)]
   [ring.util.response :refer [file-response]]))

(def channels (atom []))

(defn async-handler [ring-request]
  ;; unified API for WebSocket and HTTP long polling/streaming
  (httpkit/with-channel ring-request channel    ; get the channel
    (if (httpkit/websocket? channel)            ; if you want to distinguish them
      (httpkit/on-receive channel (fn [raw]     ; two way communication
                                    (let [data (edn/read-string raw)
                                          name (:name data)
                                          msg  (:msg data)]
                                      (info "WebSocket: " data)
                                      (if (not (seq (filter #(= % channel) @channels)))
                                        (swap! channels #(conj %1 channel)))
                                      (doseq [c @channels]
                                        (httpkit/send!
                                         c (pr-str {:msg msg
                                                    :name name
                                                    :timestamp (.toString (lt/local-now))}))))))
      (httpkit/send! channel {:status 200
                              :headers {"Content-Type" "text/plain"}
                              :body    "Long polling?"}))))

(defroutes app-routes
  (GET "/" []
       (file-response "index.html" {:root "resources/public"}))
  (GET "/async" [] async-handler) ;; asynchronous(long polling)
  (route/resources "/") ; {:root "resources"})
  (route/not-found "Not Found"))

(def app
  (-> (handler/site #'app-routes)
      (wrap-timbre {})))

(defn -main [& args]
  (httpkit/run-server app {:port 3000}))
