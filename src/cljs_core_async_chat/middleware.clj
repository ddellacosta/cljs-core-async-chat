(ns cljs-core-async-chat.middleware
  (:require
   [ring.util.response]
   [taoensso.timbre :as timbre
    :refer (trace debug info warn error fatal spy with-log-level)]))

(defn wrap-timbre [handler config]
  (fn [request]
    (let [response (handler request)]
      (when (:websocket? request)
        (info "Received WebSocket request: " request))
      (info (str "uri: " (:uri request) " - status: " (:status response)))
      response)))
