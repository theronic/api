## cljs.reader/push-back-reader



 <table border="1">
<tr>
<td>function</td>
<td><a href="https://github.com/cljsinfo/cljs-api-docs/tree/0.0-927"><img valign="middle" alt="[+] 0.0-927" title="Added in 0.0-927" src="https://img.shields.io/badge/+-0.0--927-lightgrey.svg"></a> </td>
</tr>
</table>


 <samp>
(__push-back-reader__ s)<br>
</samp>

---







Source code @ [github](https://github.com/clojure/clojurescript/blob/r1450/src/cljs/cljs/reader.cljs#L30-L32):

```clj
(defn push-back-reader [s]
  "Creates a StringPushbackReader from a given string"
  (StringPushbackReader. s (atom 0) (atom nil)))
```

<!--
Repo - tag - source tree - lines:

 <pre>
clojurescript @ r1450
└── src
    └── cljs
        └── cljs
            └── <ins>[reader.cljs:30-32](https://github.com/clojure/clojurescript/blob/r1450/src/cljs/cljs/reader.cljs#L30-L32)</ins>
</pre>

-->

---



###### External doc links:

[`cljs.reader/push-back-reader` @ crossclj](http://crossclj.info/fun/cljs.reader.cljs/push-back-reader.html)<br>

---

 <table>
<tr><td>
<img valign="middle" align="right" width="48px" src="http://i.imgur.com/Hi20huC.png">
</td><td>
Created for the upcoming ClojureScript website.<br>
[edit here] | [learn how]
</td></tr></table>

[edit here]:https://github.com/cljsinfo/cljs-api-docs/blob/master/cljsdoc/cljs.reader/push-back-reader.cljsdoc
[learn how]:https://github.com/cljsinfo/cljs-api-docs/wiki/cljsdoc-files

<!--

This information was too distracting to show to readers, but I'll leave it
commented here since it is helpful to:

- pretty-print the data used to generate this document
- and show how to retrieve that data



The API data for this symbol:

```clj
{:ns "cljs.reader",
 :name "push-back-reader",
 :type "function",
 :signature ["[s]"],
 :source {:code "(defn push-back-reader [s]\n  \"Creates a StringPushbackReader from a given string\"\n  (StringPushbackReader. s (atom 0) (atom nil)))",
          :title "Source code",
          :repo "clojurescript",
          :tag "r1450",
          :filename "src/cljs/cljs/reader.cljs",
          :lines [30 32]},
 :full-name "cljs.reader/push-back-reader",
 :full-name-encode "cljs.reader/push-back-reader",
 :history [["+" "0.0-927"]]}

```

Retrieve the API data for this symbol:

```clj
;; from Clojure REPL
(require '[clojure.edn :as edn])
(-> (slurp "https://raw.githubusercontent.com/cljsinfo/cljs-api-docs/catalog/cljs-api.edn")
    (edn/read-string)
    (get-in [:symbols "cljs.reader/push-back-reader"]))
```

-->