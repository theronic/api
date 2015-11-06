## cljs.repl.browser/send-for-eval



 <table border="1">
<tr>
<td>function</td>
<td><a href="https://github.com/cljsinfo/cljs-api-docs/tree/0.0-927"><img valign="middle" alt="[+] 0.0-927" title="Added in 0.0-927" src="https://img.shields.io/badge/+-0.0--927-lightgrey.svg"></a> </td>
</tr>
</table>


 <samp>
(__send-for-eval__ form return-value-fn)<br>
</samp>
 <samp>
(__send-for-eval__ conn form return-value-fn)<br>
</samp>

---





Source docstring:

```
Given a form and a return value function, send the form to the
browser for evaluation. The return value function will be called
when the return value is received.
```


Source code @ [github](https://github.com/clojure/clojurescript/blob/r1450/src/clj/cljs/repl/browser.clj#L100-L108):

```clj
(defn send-for-eval
  ([form return-value-fn]
     (send-for-eval @(connection) form return-value-fn))
  ([conn form return-value-fn]
     (do (set-return-value-fn return-value-fn)
         (send-and-close conn 200 form "text/javascript"))))
```

<!--
Repo - tag - source tree - lines:

 <pre>
clojurescript @ r1450
└── src
    └── clj
        └── cljs
            └── repl
                └── <ins>[browser.clj:100-108](https://github.com/clojure/clojurescript/blob/r1450/src/clj/cljs/repl/browser.clj#L100-L108)</ins>
</pre>

-->

---



###### External doc links:

[`cljs.repl.browser/send-for-eval` @ crossclj](http://crossclj.info/fun/cljs.repl.browser/send-for-eval.html)<br>

---

 <table>
<tr><td>
<img valign="middle" align="right" width="48px" src="http://i.imgur.com/Hi20huC.png">
</td><td>
Created for the upcoming ClojureScript website.<br>
[edit here] | [learn how]
</td></tr></table>

[edit here]:https://github.com/cljsinfo/cljs-api-docs/blob/master/cljsdoc/cljs.repl.browser/send-for-eval.cljsdoc
[learn how]:https://github.com/cljsinfo/cljs-api-docs/wiki/cljsdoc-files

<!--

This information was too distracting to show to readers, but I'll leave it
commented here since it is helpful to:

- pretty-print the data used to generate this document
- and show how to retrieve that data



The API data for this symbol:

```clj
{:ns "cljs.repl.browser",
 :name "send-for-eval",
 :signature ["[form return-value-fn]" "[conn form return-value-fn]"],
 :history [["+" "0.0-927"]],
 :type "function",
 :full-name-encode "cljs.repl.browser/send-for-eval",
 :source {:code "(defn send-for-eval\n  ([form return-value-fn]\n     (send-for-eval @(connection) form return-value-fn))\n  ([conn form return-value-fn]\n     (do (set-return-value-fn return-value-fn)\n         (send-and-close conn 200 form \"text/javascript\"))))",
          :title "Source code",
          :repo "clojurescript",
          :tag "r1450",
          :filename "src/clj/cljs/repl/browser.clj",
          :lines [100 108]},
 :full-name "cljs.repl.browser/send-for-eval",
 :docstring "Given a form and a return value function, send the form to the\nbrowser for evaluation. The return value function will be called\nwhen the return value is received."}

```

Retrieve the API data for this symbol:

```clj
;; from Clojure REPL
(require '[clojure.edn :as edn])
(-> (slurp "https://raw.githubusercontent.com/cljsinfo/cljs-api-docs/catalog/cljs-api.edn")
    (edn/read-string)
    (get-in [:symbols "cljs.repl.browser/send-for-eval"]))
```

-->