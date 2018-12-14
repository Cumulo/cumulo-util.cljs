
Cumulo Util (WIP)
------

> Util functions abstracted out for Cumulo projects.

### Usage

[![Clojars Project](https://img.shields.io/clojars/v/cumulo/util.svg)](https://clojars.org/cumulo/util)

```edn
[cumulo/util "0.1.1-a4"]
```

```clojure
(cumulo-util.core/find-first "x" ["x" "y"])
(cumulo-util.core/get-env! x) ; reading process.env[x]
(cumulo-util.core/id!)
(cumulo-util.core/unix-time!)
(cumulo-util.core/repeat! 1 #(println "doing")!) ; in seconds
(cumulo-util.core/delay! 1 #(println "done")!) ; in seconds

(cumulo-util.file/write-mildly! "a.text" "content")
(cumulo-util.file/get-backup-path!)
(cumulo-util.file/merge-local-edn! {} "a.edn" (fn [found?] (println found?)))
```

### License

MIT
