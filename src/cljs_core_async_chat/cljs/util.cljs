(ns cljs-core-async-chat.cljs.util)

(defn format-date [ts]
  (let [d (new js/Date (.parse js/Date ts))
        Y (.getFullYear d)
        M (+ 1 (.getMonth d))
        D (.getDate d)
        h (.getHours d)
        m (.getMinutes d)
        s (.getSeconds d)]
    (str Y "-" M "-" D " " h ":" m ":" s)))
