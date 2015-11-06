## cljs.repl/repl-prompt



 <table border="1">
<tr>
<td>function</td>
<td><a href="https://github.com/cljsinfo/cljs-api-docs/tree/0.0-2911"><img valign="middle" alt="[+] 0.0-2911" title="Added in 0.0-2911" src="https://img.shields.io/badge/+-0.0--2911-lightgrey.svg"></a> </td>
</tr>
</table>


 <samp>
(__repl-prompt__)<br>
</samp>

---







Source code @ [github](https://github.com/clojure/clojurescript/blob/r3149/src/clj/cljs/repl.clj#L670-L671):

```clj
(defn repl-prompt []
  (print (str "ClojureScript:" ana/*cljs-ns* "> ")))
```

<!--
Repo - tag - source tree - lines:

 <pre>
clojurescript @ r3149
└── src
    └── clj
        └── cljs
            └── <ins>[repl.clj:670-671](https://github.com/clojure/clojurescript/blob/r3149/src/clj/cljs/repl.clj#L670-L671)</ins>
</pre>

-->

---



###### External doc links:

[`cljs.repl/repl-prompt` @ crossclj](http://crossclj.info/fun/cljs.repl/repl-prompt.html)<br>

---

 <table>
<tr><td>
<img valign="middle" align="right" width="48px" src="http://i.imgur.com/Hi20huC.png">
</td><td>
Created for the upcoming ClojureScript website.<br>
[edit here] | [learn how]
</td></tr></table>

[edit here]:https://github.com/cljsinfo/cljs-api-docs/blob/master/cljsdoc/cljs.repl/repl-prompt.cljsdoc
[learn how]:https://github.com/cljsinfo/cljs-api-docs/wiki/cljsdoc-files

<!--

This information was too distracting to show to readers, but I'll leave it
commented here since it is helpful to:

- pretty-print the data used to generate this document
- and show how to retrieve that data



The API data for this symbol:

```clj
{:ns "cljs.repl",
 :name "repl-prompt",
 :type "function",
 :signature ["[]"],
 :source {:code "(defn repl-prompt []\n  (print (str \"ClojureScript:\" ana/*cljs-ns* \"> \")))",
          :title "Source code",
          :repo "clojurescript",
          :tag "r3149",
          :filename "src/clj/cljs/repl.clj",
          :lines [670 671]},
 :full-name "cljs.repl/repl-prompt",
 :full-name-encode "cljs.repl/repl-prompt",
 :history [["+" "0.0-2911"]]}

```

Retrieve the API data for this symbol:

```clj
;; from Clojure REPL
(require '[clojure.edn :as edn])
(-> (slurp "https://raw.githubusercontent.com/cljsinfo/cljs-api-docs/catalog/cljs-api.edn")
    (edn/read-string)
    (get-in [:symbols "cljs.repl/repl-prompt"]))
```

-->