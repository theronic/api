## <img width="48px" valign="middle" src="http://i.imgur.com/Hi20huC.png"> cljs.core/dec

 <table border="1">
<tr>
<td>function</td>
<td><a href="https://github.com/cljsinfo/api-refs/tree/0.0-927"><img valign="middle" alt="[+] 0.0-927" src="https://img.shields.io/badge/+-0.0--927-lightgrey.svg"></a> </td>
<td>
[<img height="24px" valign="middle" src="http://i.imgur.com/1GjPKvB.png"> <samp>clojure.core/dec</samp>](http://clojure.github.io/clojure/branch-master/clojure.core-api.html#clojure.core/dec)
</td>
</tr>
</table>

 <samp>
(__dec__ x)<br>
</samp>

```
Returns a number one less than num.
```

---

 <pre>
clojurescript @ r1443
└── src
    └── cljs
        └── cljs
            └── <ins>[core.cljs:1258-1260](https://github.com/clojure/clojurescript/blob/r1443/src/cljs/cljs/core.cljs#L1258-L1260)</ins>
</pre>

```clj
(defn dec
  [x] (- x 1))
```


---

 <pre>
clojurescript @ r1443
└── src
    └── clj
        └── cljs
            └── <ins>[core.clj:268-269](https://github.com/clojure/clojurescript/blob/r1443/src/clj/cljs/core.clj#L268-L269)</ins>
</pre>

```clj
(defmacro dec [x]
  `(- ~x 1))
```

---

```clj
{:ns "cljs.core",
 :name "dec",
 :signature ["[x]"],
 :shadowed-sources ({:code "(defmacro dec [x]\n  `(- ~x 1))",
                     :filename "clojurescript/src/clj/cljs/core.clj",
                     :lines [268 269],
                     :link "https://github.com/clojure/clojurescript/blob/r1443/src/clj/cljs/core.clj#L268-L269"}),
 :history [["+" "0.0-927"]],
 :type "function",
 :full-name-encode "cljs.core_dec",
 :source {:code "(defn dec\n  [x] (- x 1))",
          :filename "clojurescript/src/cljs/cljs/core.cljs",
          :lines [1258 1260],
          :link "https://github.com/clojure/clojurescript/blob/r1443/src/cljs/cljs/core.cljs#L1258-L1260"},
 :full-name "cljs.core/dec",
 :clj-symbol "clojure.core/dec",
 :docstring "Returns a number one less than num."}

```