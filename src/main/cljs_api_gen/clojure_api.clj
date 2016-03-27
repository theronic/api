(ns cljs-api-gen.clojure-api
  (:require
    [clansi.core :refer [style]]
    [clojure.set :refer [difference]]
    [cljs-api-gen.repo-cljs :refer [*clj-tag* ls-files clj-tag->api-key]]
    [cljs-api-gen.syntax :refer [syntax-map]]
    [me.raynes.fs :refer [exists? base-name]]
    ))

;;--------------------------------------------------------------------------------
;; Official Clojure API
;;--------------------------------------------------------------------------------

(def versions ["1.3" "1.4" "1.5" "1.6" "1.7" "1.8"])
(def api-namespaces (atom {}))
(def api-symbols (atom {}))

(defn api-cache [v]
  (str "clj-api-" v ".clj"))

(defn version-api-url [v]
  (str "https://raw.githubusercontent.com/clojure/clojure/gh-pages/index-v" v ".clj"))

(defn get-version-apis! []
  (println (style "\nRetrieving Clojure API files...\n" :cyan))
  (doseq [v versions]
    (println " Clojure" v "API...")
    (let [cache-filename (api-cache v)]
      (when-not (exists? cache-filename)
        (spit cache-filename (slurp (version-api-url v))))
      (let [data (read-string (slurp cache-filename))
            namespaces (->> (:namespaces data)
                            (map :name)
                            set)
            symbols (->> (:vars data)
                         (map #(str (:namespace %) "/" (:name %)))
                         set)]
        (swap! api-symbols assoc v symbols)
        (swap! api-namespaces assoc v namespaces)))))

;;--------------------------------------------------------------------------------
;; Clojure's Types and Protocols
;;
;;  - `clojure.lang` namespace (non-public)
;;  - `clojure.lang.PersistentQueue/EMPTY` has to be used to create queues,
;;    so might as well bring all the types in and relate them to the cljs
;;--------------------------------------------------------------------------------

(def lang-symbols->parent
  "These clojure.lang symbols don't have their own file.
  They belong in the files named after their respective mapped values."

  {"ArrayNode"                "PersistentHashMap"
   "BitmapIndexedNode"        "PersistentHashMap"
   "EmptyList"                "PersistentList"

   "HashCollisionNode"        "PersistentHashmap"
   "KeySeq"                   "APersistentMap"
   "RSeq"                     "APersistentVector"
   "ValSeq"                   "APersistentMap"
   "NodeSeq"                  "PersistentHashMap"
   "ChunkedSeq"               "PersistentVector"

   "TransientArrayMap"        "PersistentArrayMap"
   "TransientHashMap"         "PersistentHashMap"
   "TransientHashSet"         "PersistentHashSet"
   "TransientVector"          "PersistentVector"

   "BitmapIndexedNode.EMPTY"  "PersistentHashMap"
   "PersistentList.EMPTY"     "PersistentList"
   "PersistentArrayMap.EMPTY" "PersistentArrayMap"
   "PersistentHashMap.EMPTY"  "PersistentHashMap"
   "PersistentHashSet.EMPTY"  "PersistentHashSet"
   "PersistentQueue.EMPTY"    "PersistentQueue"
   "PersistentTreeMap.EMPTY"  "PersistentTreeMap"
   "PersistentTreeSet.EMPTY"  "PersistentTreeSet"
   "PersistentVector.EMPTY"   "PersistentVector"
   })

(def lang-symbols (atom {}))
(def lang-path "src/jvm/clojure/lang/")
(defn get-lang-symbols! [tag]
  (if-let [symbols (@lang-symbols tag)]
    symbols
    (let [ns- "clojure.lang"
          symbols (->> (ls-files "clojure" tag lang-path)
                       (filter #(.endsWith % ".java"))
                       (map #(str ns- "/" (base-name % true)))
                       (concat (map #(str ns- "/" %) (keys lang-symbols->parent)))
                       set)]
      (swap! lang-symbols assoc tag symbols)
      symbols)))

;;--------------------------------------------------------------------------------
;; ClojureScript -> Clojure name mapping
;;--------------------------------------------------------------------------------

(def cljs-ns->clj
  {"cljs.core"   "clojure.core"
   "cljs.pprint" "clojure.pprint"
   "cljs.test"   "clojure.test"
   "cljs.repl"   "clojure.repl"
   "special"     "clojure.core"
   "specialrepl" "clojure.core"
   })

(def cljs-full-name->clj
  "cljs symbols that map to different clj names."

  {;; library
   "cljs.core/*clojurescript-version*"  "clojure.core/*clojure-version*"
   "cljs.reader/read-string"            "clojure.core/read-string"
   "cljs.reader/read"                   "clojure.core/read"

   ;; compiler
   "cljs.analyzer.api/all-ns"           "clojure.core/all-ns"
   "cljs.analyzer.api/find-ns"          "clojure.core/find-ns"
   "cljs.analyzer.api/ns-interns"       "clojure.core/ns-interns"
   "cljs.analyzer.api/ns-publics"       "clojure.core/ns-publics"
   "cljs.analyzer.api/ns-resolve"       "clojure.core/ns-resolve"
   "cljs.analyzer.api/remove-ns"        "clojure.core/remove-ns"
   "cljs.analyzer.api/resolve"          "clojure.core/resolve"

   ;; protocols
   "cljs.core/IAssociative"             "clojure.lang/Associative"
   "cljs.core/ICounted"                 "clojure.lang/Counted"
   "cljs.core/IIndexed"                 "clojure.lang/Indexed"
   "cljs.core/IList"                    "clojure.lang/IPersistentList"
   "cljs.core/INamed"                   "clojure.lang/Named"
   "cljs.core/IReversible"              "clojure.lang/Reversible"
   "cljs.core/ISeqable"                 "clojure.lang/Seqable"
   "cljs.core/ISequential"              "clojure.lang/Sequential"
   "cljs.core/ISet"                     "clojure.lang/IPersistentSet"
   "cljs.core/ISorted"                  "clojure.lang/Sorted"
   "cljs.core/IStack"                   "clojure.lang/IPersistentStack"
   "cljs.core/IVector"                  "clojure.lang/IPersistentVector"

   ;; types
   "cljs.core/List"                     "clojure.lang/PersistentList"
   "cljs.core/SeqIter"                  "clojure.lang/SeqIterator"

   ;; member attributes
   "cljs.core/List.EMPTY"               "clojure.lang/PersistentList.EMPTY"
   })

(defn clj-lookup-name
  "Map a parsed ClojureScript item to a related Clojure name to be looked up for resolution."
  [item]
  (or ;; use custom name mapping if found
      (cljs-full-name->clj (:full-name item))

      ;; map to clojure.lang namespace
      (when (and (= "cljs.core" (:ns item))
                 (or (:parent-type item)
                     (#{"type" "protocol"} (:type item))))
        (str "clojure.lang/" (:name item)))

      ;; use custom namespace mapping if found
      (when-let [clj-ns (cljs-ns->clj (:ns item))]
        (str clj-ns "/" (:name item)))

      ;; default to use full name unmodified
      (:full-name item)))

(defn attach-clj-symbol
  "For the given API entry item, attach a :clj-symbol (full-name) for the related Clojure symbol."
  [item]
  (let [clj-version (clj-tag->api-key *clj-tag*)
        clj-symbol? (get @api-symbols clj-version)
        lang-symbol? (get-lang-symbols! *clj-tag*)
        lookup-name (clj-lookup-name item)]
    (if (or (lang-symbol? lookup-name)
            (clj-symbol? lookup-name))
      (assoc item :clj-symbol lookup-name)
      item)))

(defn attach-clj-ns
  [item]
  (let [clj-version (clj-tag->api-key *clj-tag*)
        clj-ns? (get @api-namespaces clj-version)
        ns- (:ns item)
        lookup-name (or (cljs-ns->clj ns-) ns-)]
    (if (clj-ns? lookup-name)
      (assoc item :clj-ns lookup-name)
      item)))

(defn get-clojure-symbols-not-in-items
  [items]
  (let [clj-symbols (into (get @api-symbols (clj-tag->api-key *clj-tag*))
                          (get @lang-symbols *clj-tag*))
        cljs-symbols (set (map clj-lookup-name items))]
    (difference clj-symbols cljs-symbols)))
