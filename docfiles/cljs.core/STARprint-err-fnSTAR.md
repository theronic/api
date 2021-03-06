---
name: cljs.core/*print-err-fn*
see also:
  - cljs.core/*print-fn*
  - cljs.core/enable-console-print!
---

## Summary

A var representing the function used to print error output, which may differ
between runtime environments. Use [doc:cljs.core/enable-console-print!] to
set it to print to console/stderr, then bind as follows:

```clj
(enable-console-print!)
(binding [*print-fn* *print-err-fn*]
  (println "printed to stderr in Node, or to browser console as error"))
```

## Details

This is meant to replace a use-case of Clojure's `*err*` var,
allowing you to print things to stderr using the print function.

```clj
;; Clojure
(binding [*out* *err]
  (println "error"))

;; ClojureScript
(binding [*print-fn* *print-err-fn*]
  (println "error"))
```

## Examples

<!-- AUTO-GENERATED docfile links for github -->
[doc:cljs.core/enable-console-print!]:https://github.com/cljs/api/blob/master/docfiles/cljs.core/enable-console-printBANG.md
