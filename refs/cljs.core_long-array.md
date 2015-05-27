## <img width="48px" valign="middle" src="http://i.imgur.com/Hi20huC.png"> cljs.core/long-array

 <table border="1">
<tr>
<td>function</td>
<td><a href="https://github.com/cljsinfo/api-refs/tree/0.0-1211"><img valign="middle" alt="[+] 0.0-1211" src="https://img.shields.io/badge/+-0.0--1211-lightgrey.svg"></a> </td>
<td>
[<img height="24px" valign="middle" src="http://i.imgur.com/1GjPKvB.png"> <samp>clojure.core/long-array</samp>](http://clojure.github.io/clojure/branch-master/clojure.core-api.html#clojure.core/long-array)
</td>
</tr>
</table>

 <samp>
(__long-array__ size-or-seq)<br>
(__long-array__ size init-val-or-seq)<br>
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
            └── <ins>[core.cljs:1936-1955](https://github.com/clojure/clojurescript/blob/r1443/src/cljs/cljs/core.cljs#L1936-L1955)</ins>
</pre>

```clj
(defn long-array
  ([size-or-seq]
     (cond
      (number? size-or-seq) (long-array size-or-seq nil)
      (seq? size-or-seq) (into-array size-or-seq)
      :else (throw (js/Error. "long-array called with something other than size or ISeq"))))
  ([size init-val-or-seq]
     (let [a (make-array size)]
       (if (seq? init-val-or-seq)
         (let [s (seq init-val-or-seq)]
           (loop [i 0 s s]
             (if (and s (< i size))
               (do
                 (aset a i (first s))
                 (recur (inc i) (next s)))
               a)))
         (do
           (dotimes [i size]
             (aset a i init-val-or-seq))
           a)))))
```


---

```clj
{:full-name "cljs.core/long-array",
 :ns "cljs.core",
 :name "long-array",
 :type "function",
 :signature ["[size-or-seq]" "[size init-val-or-seq]"],
 :source {:code "(defn long-array\n  ([size-or-seq]\n     (cond\n      (number? size-or-seq) (long-array size-or-seq nil)\n      (seq? size-or-seq) (into-array size-or-seq)\n      :else (throw (js/Error. \"long-array called with something other than size or ISeq\"))))\n  ([size init-val-or-seq]\n     (let [a (make-array size)]\n       (if (seq? init-val-or-seq)\n         (let [s (seq init-val-or-seq)]\n           (loop [i 0 s s]\n             (if (and s (< i size))\n               (do\n                 (aset a i (first s))\n                 (recur (inc i) (next s)))\n               a)))\n         (do\n           (dotimes [i size]\n             (aset a i init-val-or-seq))\n           a)))))",
          :filename "clojurescript/src/cljs/cljs/core.cljs",
          :lines [1936 1955],
          :link "https://github.com/clojure/clojurescript/blob/r1443/src/cljs/cljs/core.cljs#L1936-L1955"},
 :full-name-encode "cljs.core_long-array",
 :clj-symbol "clojure.core/long-array",
 :history [["+" "0.0-1211"]]}

```