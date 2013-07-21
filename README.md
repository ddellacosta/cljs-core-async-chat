# cljs-core-async-chat

A simple demo using httpkit and core.async to build a WebSocket-based, extremely dumb chat system.

The usual caveats in terms of browser-compatibility with WebSocket applies.

## Prerequisites

You will need [Leiningen][1] 1.7.0 or above installed.

[1]: https://github.com/technomancy/leiningen

## Running

### Compile CLJS

    lein cljsbuild once

To start a web server for the application, run:

    lein run

Go to localhost:3000.

Easier to see what's going on if you open it on a few different machines on the same network (will need to configure ws-url in base.cljs), or at least a few different browsers on the same machine.

## License

Distributed under the MIT License (http://dd.mit-license.org/)

Copyright © 2013 Dave Della Costa
