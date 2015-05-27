## <img width="48px" valign="middle" src="http://i.imgur.com/Hi20huC.png"> cljs.core/ISeqable

 <table border="1">
<tr>
<td>protocol</td>
<td><a href="https://github.com/cljsinfo/api-refs/tree/0.0-927"><img valign="middle" alt="[+] 0.0-927" src="https://img.shields.io/badge/+-0.0--927-lightgrey.svg"></a> </td>
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
            └── <ins>[core.cljs:217-218](https://github.com/clojure/clojurescript/blob/r1443/src/cljs/cljs/core.cljs#L217-L218)</ins>
</pre>

```clj
(defprotocol ISeqable
  (-seq [o]))
```


---

```clj
{:ns "cljs.core",
 :name "ISeqable",
 :type "protocol",
 :full-name-encode "cljs.core_ISeqable",
 :source {:code "(defprotocol ISeqable\n  (-seq [o]))",
          :filename "clojurescript/src/cljs/cljs/core.cljs",
          :lines [217 218],
          :link "https://github.com/clojure/clojurescript/blob/r1443/src/cljs/cljs/core.cljs#L217-L218"},
 :methods [{:name "-seq", :signature ["[o]"], :docstring nil}],
 :full-name "cljs.core/ISeqable",
 :history [["+" "0.0-927"]]}

```