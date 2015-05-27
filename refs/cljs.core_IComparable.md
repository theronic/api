## <img width="48px" valign="middle" src="http://i.imgur.com/Hi20huC.png"> cljs.core/IComparable

 <table border="1">
<tr>
<td>protocol</td>
<td><a href="https://github.com/cljsinfo/api-refs/tree/0.0-1424"><img valign="middle" alt="[+] 0.0-1424" src="https://img.shields.io/badge/+-0.0--1424-lightgrey.svg"></a> </td>
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
            └── <ins>[core.cljs:269-270](https://github.com/clojure/clojurescript/blob/r1443/src/cljs/cljs/core.cljs#L269-L270)</ins>
</pre>

```clj
(defprotocol IComparable
  (-compare [x y]))
```


---

```clj
{:ns "cljs.core",
 :name "IComparable",
 :type "protocol",
 :full-name-encode "cljs.core_IComparable",
 :source {:code "(defprotocol IComparable\n  (-compare [x y]))",
          :filename "clojurescript/src/cljs/cljs/core.cljs",
          :lines [269 270],
          :link "https://github.com/clojure/clojurescript/blob/r1443/src/cljs/cljs/core.cljs#L269-L270"},
 :methods [{:name "-compare", :signature ["[x y]"], :docstring nil}],
 :full-name "cljs.core/IComparable",
 :history [["+" "0.0-1424"]]}

```