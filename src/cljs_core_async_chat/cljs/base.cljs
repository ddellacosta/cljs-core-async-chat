(ns cljs-core-async-chat.cljs.base
  (:require
   [domina :as dom]
   [domina.css :as css]
   [domina.events :as events]
   [cljs.core.async :refer [chan <! >! put!]]
   [cljs.reader :as reader]
   [cljs-core-async.cljs.util :as util]
  (:require-macros
   [cljs.core.async.macros :refer [go]]))

(def send (chan))
(def receive (chan))
(def alert-view (chan))

(def ws-url "ws://localhost:3000/async")
(def ws (new js/WebSocket ws-url))

;; Lifted almost completely from dnolen: 
;;  https://github.com/swannodette/async-tests/blob/master/src/async_test/utils/helpers.cljs#L103
(defn event-chan
  [c el type]
  (let [writer #(put! c %)]
    (events/listen! el type writer)
    {:chan c
     :unsubscribe #(.removeEventListener el type writer)}))

(defn my-name []
  (let [name (-> "input#name" css/sel dom/single-node dom/value)]
    (if (clojure.string/blank? name) "Anonymous" name)))

(defn make-sender []
  (event-chan send (css/sel "input#send") :click)
  (event-chan send (css/sel "input#message") :keypress)
  (go
   (while true
     (let [evt  (<! send)
           name (my-name)
           msg  (-> "input#message" css/sel dom/single-node dom/value)]
       (when (or (= (events/event-type evt) "click")
                 (= (.-keyCode (events/raw-event evt)) 13))
         (.send ws {:msg msg :name name}))))))

(defn msg-template [data]
  (let [me   (if (= (my-name) (:name data)) " me")
        ts   (util/format-date (:timestamp data))
        name (:name data)
        msg  (:msg data)]
    (str "<div class=\"msg new" me "\">"
         "<span class=\"time\">" ts
         "</span><span class=\"name\">" name
         "</span><span class=\"msg\">"  msg
         "</span></div>")))

(defn messages []
  (css/sel "div#received-msg-wrapper div#messages"))

(defn add-message []
  (go
   (while true
     (let [msg            (<! receive)
           raw-data       (.-data msg)
           data           (reader/read-string raw-data)
           templated-data (msg-template data)]
       (dom/insert! (messages) templated-data)
       (>! alert-view (-> (messages) dom/children last))))))

(defn highlight-new-message []
  (go
   (while true
     (let [alert-msg (<! alert-view)]
       (js/setTimeout
        #(dom/remove-class! alert-msg "new")
        10)))))

(defn make-receiver []
  (set! (.-onmessage ws) (fn [msg] (put! receive msg)))
  (add-message)
  (highlight-new-message))

(defn init! []
  (make-sender)
  (make-receiver))

(def on-load
  (set! (.-onload js/window) init!))
