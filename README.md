
Cumulo Util (WIP)
------

> Util functions abstracted out for Cumulo projects.

### Usage

[![Clojars Project](https://img.shields.io/clojars/v/cumulo/util.svg)](https://clojars.org/cumulo/util)

```edn
[cumulo/util "0.1.11-a1"]
```

Notice that you will need `shortid` in your project since it's depended on:

```bash
yarn add shortid
```

```clojure
(cumulo-util.core/find-first "x" ["x" "y"])
(cumulo-util.core/get-env! x) ; reading process.env[x]
(cumulo-util.core/id!)
(cumulo-util.core/unix-time!)
(cumulo-util.core/repeat! 1 #(println "doing")!) ; in seconds
(cumulo-util.core/delay! 1 #(println "done")!) ; in seconds
(cumulo-util.core/on-page-touch (fn [] (println "touched")))

(cumulo-util.file/write-mildly! "a.text" "content")
(cumulo-util.file/get-backup-path!)
(cumulo-util.file/merge-local-edn! {} "a.edn" (fn [found?] (println found?)))
(cumulo-util.file/sh! "ls") ; run shell command
(cumulo-util.file/chan-pick-port 7000) ; detect 7000, if inuse then inc the port

; macros
(cumulo-util.build/get-ip!)
(cumulo-util.build/inline-resource "a.text")

(cumulo-util.async/all-once get-chan [1 2 3 4]) ; like Promise.all
```

### License

MIT
