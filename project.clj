(defproject cljs-core-async-chat "0.1.0-SNAPSHOT"
  :description "simple demo of WebSocket-based chat using httpkit and core.async"
  :url "https://github.com/ddellacosta/cljs-core-async-chat"

  :repositories {"sonatype-oss-public"
                 "https://oss.sonatype.org/content/groups/public/"}

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.5"]
                 [org.clojure/core.async "0.1.0-SNAPSHOT"]
                 [ring "1.2.0"]
                 [domina "1.0.2-SNAPSHOT"]
                 [com.taoensso/timbre "2.3.0"]
                 [http-kit "2.1.7"]
                 [clj-time "0.5.1"]
                 [com.cemerick/piggieback "0.0.5"] ; if I don't have this here I can't load repl with dev profile?
                 [org.clojure/clojurescript "0.0-1844"]]

  :plugins [[lein-cljsbuild "0.3.2"]]

  :main cljs-core-async-chat.handler

  :profiles
  {:dev
   {:dependencies [[ring-mock "0.1.5"]
                   [com.cemerick/clojurescript.test "0.0.4"]
                   [com.cemerick/piggieback "0.0.5"]
                   [org.clojure/tools.nrepl "0.2.3" :exclusions [org.clojure/clojure]]]
   :repl-options
   {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}}}

  :cljsbuild
  {:builds
   [{:id "dev"
     :source-paths ["src/cljs_core_async_chat/cljs"]
     :compiler {:output-to "resources/public/js/cljs.js"
                :optimizations :whitespace
                :pretty-print true}}
    {:id "cljs-repl"
     :source-paths ["src/cljs_core_async_chat/cljs"
                    "src/cljs_core_async_chat/browser"]
     :compiler {:output-to "resources/public/js/cljs.js"
                :optimizations :whitespace
                :pretty-print true}}]})
