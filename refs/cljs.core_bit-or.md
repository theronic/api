## <img width="48px" valign="middle" src="http://i.imgur.com/Hi20huC.png"> cljs.core/bit-or

 <table border="1">
<tr>
<td>function</td>
<td><a href="https://github.com/cljsinfo/api-refs/tree/0.0-927"><img valign="middle" alt="[+] 0.0-927" src="https://img.shields.io/badge/+-0.0--927-lightgrey.svg"></a> </td>
<td>
[<img height="24px" valign="middle" src="http://i.imgur.com/1GjPKvB.png"> <samp>clojure.core/bit-or</samp>](http://clojure.github.io/clojure/branch-master/clojure.core-api.html#clojure.core/bit-or)
</td>
</tr>
</table>

 <samp>
(__bit-or__ x y)<br>
</samp>

```
Bitwise or
```

---

 <pre>
clojurescript @ r1443
└── src
    └── cljs
        └── cljs
            └── <ins>[core.cljs:1325-1327](https://github.com/clojure/clojurescript/blob/r1443/src/cljs/cljs/core.cljs#L1325-L1327)</ins>
</pre>

```clj
(defn bit-or
  [x y] (cljs.core/bit-or x y))
```


---

 <pre>
clojurescript @ r1443
└── src
    └── clj
        └── cljs
            └── <ins>[core.clj:308-310](https://github.com/clojure/clojurescript/blob/r1443/src/clj/cljs/core.clj#L308-L310)</ins>
</pre>

```clj
(defmacro bit-or
  ([x y] (list 'js* "(~{} | ~{})" x y))
  ([x y & more] `(bit-or (bit-or ~x ~y) ~@more)))
```

---

```clj
{:ns "cljs.core",
 :name "bit-or",
 :signature ["[x y]"],
 :shadowed-sources ({:code "(defmacro bit-or\n  ([x y] (list 'js* \"(~{} | ~{})\" x y))\n  ([x y & more] `(bit-or (bit-or ~x ~y) ~@more)))",
                     :filename "clojurescript/src/clj/cljs/core.clj",
                     :lines [308 310],
                     :link "https://github.com/clojure/clojurescript/blob/r1443/src/clj/cljs/core.clj#L308-L310"}),
 :history [["+" "0.0-927"]],
 :type "function",
 :full-name-encode "cljs.core_bit-or",
 :source {:code "(defn bit-or\n  [x y] (cljs.core/bit-or x y))",
          :filename "clojurescript/src/cljs/cljs/core.cljs",
          :lines [1325 1327],
          :link "https://github.com/clojure/clojurescript/blob/r1443/src/cljs/cljs/core.cljs#L1325-L1327"},
 :full-name "cljs.core/bit-or",
 :clj-symbol "clojure.core/bit-or",
 :docstring "Bitwise or"}

```