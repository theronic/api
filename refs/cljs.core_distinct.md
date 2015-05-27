## <img width="48px" valign="middle" src="http://i.imgur.com/Hi20huC.png"> cljs.core/distinct

 <table border="1">
<tr>
<td>function</td>
<td><a href="https://github.com/cljsinfo/api-refs/tree/0.0-927"><img valign="middle" alt="[+] 0.0-927" src="https://img.shields.io/badge/+-0.0--927-lightgrey.svg"></a> </td>
<td>
[<img height="24px" valign="middle" src="http://i.imgur.com/1GjPKvB.png"> <samp>clojure.core/distinct</samp>](http://clojure.github.io/clojure/branch-master/clojure.core-api.html#clojure.core/distinct)
</td>
</tr>
</table>

 <samp>
(__distinct__ coll)<br>
</samp>

```
Returns a lazy sequence of the elements of coll with duplicates removed
```

---

 <pre>
clojurescript @ r1443
└── src
    └── cljs
        └── cljs
            └── <ins>[core.cljs:5760-5771](https://github.com/clojure/clojurescript/blob/r1443/src/cljs/cljs/core.cljs#L5760-L5771)</ins>
</pre>

```clj
(defn distinct
  [coll]
  (let [step (fn step [xs seen]
               (lazy-seq
                ((fn [[f :as xs] seen]
                   (when-let [s (seq xs)]
                     (if (contains? seen f)
                       (recur (rest s) seen)
                       (cons f (step (rest s) (conj seen f))))))
                 xs seen)))]
    (step coll #{})))
```


---

```clj
{:ns "cljs.core",
 :name "distinct",
 :signature ["[coll]"],
 :history [["+" "0.0-927"]],
 :type "function",
 :full-name-encode "cljs.core_distinct",
 :source {:code "(defn distinct\n  [coll]\n  (let [step (fn step [xs seen]\n               (lazy-seq\n                ((fn [[f :as xs] seen]\n                   (when-let [s (seq xs)]\n                     (if (contains? seen f)\n                       (recur (rest s) seen)\n                       (cons f (step (rest s) (conj seen f))))))\n                 xs seen)))]\n    (step coll #{})))",
          :filename "clojurescript/src/cljs/cljs/core.cljs",
          :lines [5760 5771],
          :link "https://github.com/clojure/clojurescript/blob/r1443/src/cljs/cljs/core.cljs#L5760-L5771"},
 :full-name "cljs.core/distinct",
 :clj-symbol "clojure.core/distinct",
 :docstring "Returns a lazy sequence of the elements of coll with duplicates removed"}

```