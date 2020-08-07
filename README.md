# cljs-core-async-chat

A simple demo using [httpkit][1] and [core.async][2] to build a WebSocket-based, extremely dumb chat system.  Uses a fair bit of [domina][4] too, I should mention.

The usual caveats in terms of browser-compatibility with WebSocket applies.

Thanks go to [David Nolen](https://github.com/swannodette) for his [copious examples][3] of how to use core.async in ClojureScript (and all his work on CLJS and core.async!), as well as random help in IRC when first getting going.

## Todos

* Properly setup/teardown WebSocket connection. Kind of a lazy hack now.
* Get core.async working with httpkit on the server side.
* See if I can take better advantage of core.async doing DOM manipulation, among other things...

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

[1]: http://http-kit.org/index.html
[2]: https://github.com/clojure/core.async
[3]: https://github.com/swannodette/async-tests
[4]: https://github.com/levand/domina
