## <img width="48px" valign="middle" src="http://i.imgur.com/Hi20huC.png"> cljs.core/meta

 <table border="1">
<tr>
<td>function</td>
<td><a href="https://github.com/cljsinfo/api-refs/tree/0.0-927"><img valign="middle" alt="[+] 0.0-927" src="https://img.shields.io/badge/+-0.0--927-lightgrey.svg"></a> </td>
<td>
[<img height="24px" valign="middle" src="http://i.imgur.com/1GjPKvB.png"> <samp>clojure.core/meta</samp>](http://clojure.github.io/clojure/branch-master/clojure.core-api.html#clojure.core/meta)
</td>
</tr>
</table>

 <samp>
(__meta__ o)<br>
</samp>

```
Returns the metadata of obj, returns nil if there is no metadata.
```

---

 <pre>
clojurescript @ r1443
└── src
    └── cljs
        └── cljs
            └── <ins>[core.cljs:807-811](https://github.com/clojure/clojurescript/blob/r1443/src/cljs/cljs/core.cljs#L807-L811)</ins>
</pre>

```clj
(defn meta
  [o]
  (when (satisfies? IMeta o)
    (-meta o)))
```


---

```clj
{:ns "cljs.core",
 :name "meta",
 :signature ["[o]"],
 :history [["+" "0.0-927"]],
 :type "function",
 :full-name-encode "cljs.core_meta",
 :source {:code "(defn meta\n  [o]\n  (when (satisfies? IMeta o)\n    (-meta o)))",
          :filename "clojurescript/src/cljs/cljs/core.cljs",
          :lines [807 811],
          :link "https://github.com/clojure/clojurescript/blob/r1443/src/cljs/cljs/core.cljs#L807-L811"},
 :full-name "cljs.core/meta",
 :clj-symbol "clojure.core/meta",
 :docstring "Returns the metadata of obj, returns nil if there is no metadata."}

```