## <img width="48px" valign="middle" src="http://i.imgur.com/Hi20huC.png"> cljs.core/IReversible

 <table border="1">
<tr>
<td>protocol</td>
<td><a href="https://github.com/cljsinfo/api-refs/tree/0.0-1211"><img valign="middle" alt="[+] 0.0-1211" src="https://img.shields.io/badge/+-0.0--1211-lightgrey.svg"></a> </td>
</tr>
</table>

 <samp>
</samp>

```
(no docstring)
```

---

 <pre>
clojurescript @ r1443
└── src
    └── cljs
        └── cljs
            └── <ins>[core.cljs:229-230](https://github.com/clojure/clojurescript/blob/r1443/src/cljs/cljs/core.cljs#L229-L230)</ins>
</pre>

```clj
(defprotocol IReversible
  (-rseq [coll]))
```


---

```clj
{:ns "cljs.core",
 :name "IReversible",
 :type "protocol",
 :full-name-encode "cljs.core_IReversible",
 :source {:code "(defprotocol IReversible\n  (-rseq [coll]))",
          :filename "clojurescript/src/cljs/cljs/core.cljs",
          :lines [229 230],
          :link "https://github.com/clojure/clojurescript/blob/r1443/src/cljs/cljs/core.cljs#L229-L230"},
 :methods [{:name "-rseq", :signature ["[coll]"], :docstring nil}],
 :full-name "cljs.core/IReversible",
 :history [["+" "0.0-1211"]]}

```