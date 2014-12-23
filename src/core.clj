(ns core
  (:refer-clojure :exclude [replace])
  (:require
    [clojure.java.io :as io]
    [clojure.java.shell :refer [sh]]
    [clojure.tools.reader :as reader]
    [clojure.tools.reader.reader-types :as readers]
    [clojure.string :refer [split split-lines join replace trim]]
    [clojure.data :refer [diff]]
    [clojure.core.match :refer [match]]
    [cljs.tagged-literals :refer [*cljs-data-readers*]]
    [me.raynes.fs :refer [mkdir exists?]]))

;; location of the clojure & clojurescript repos to parse
(def repo-dir "repos")

(def docs-repo-dir "cljs-api-docs")

(def docs-branch "docs")

;; location of the documents generated by this program
(def ^:dynamic *output-dir* nil)

;; SHA1 hashes of the checked out commits of the language repos
(def ^:dynamic *repo-version*
  {"clojure" nil
   "clojurescript" nil})

;; functions marked as macros
(def ^:dynamic *fn-macros* [])

;; Table of namespaces that we will parse
(def cljs-ns-paths
  ; NS                        REPO             FILE               PATH IN REPO
  {"cljs.core"              {"clojurescript" {"core.cljs"        "src/cljs/cljs"
                                              "core.clj"         "src/clj/cljs"
                                              "analyzer.clj"     "src/clj/cljs"
                                              "compiler.clj"     "src/clj/cljs"}
                             "clojure"       {"core.clj"         "src/clj/clojure"
                                              "core_deftype.clj" "src/clj/clojure"
                                              "core_print.clj"   "src/clj/clojure"
                                              "core_proxy.clj"   "src/clj/clojure"}}
   "cljs.test"              {"clojurescript" {"test.cljs"        "src/cljs/cljs"
                                              "test.clj"         "src/clj/cljs"}}
   "cljs.reader"            {"clojurescript" {"reader.cljs"      "src/cljs/cljs"}}
   "clojure.set"            {"clojurescript" {"set.cljs"         "src/cljs/clojure"}}
   "clojure.string"         {"clojurescript" {"string.cljs"      "src/cljs/clojure"}}
   "clojure.walk"           {"clojurescript" {"walk.cljs"        "src/cljs/clojure"}}
   "clojure.zip"            {"clojurescript" {"zip.cljs"         "src/cljs/clojure"}}
   "clojure.data"           {"clojurescript" {"data.cljs"        "src/cljs/clojure"}}})

;;------------------------------------------------------------
;; Form Reading
;;------------------------------------------------------------

(defn read-forms
  [r]
  (loop [forms (transient [])]
    (if-let [f (try (binding [reader/*data-readers* *cljs-data-readers*]
                      (reader/read r))
                    (catch Exception e
                      (when-not (= (.getMessage e) "EOF") (throw e))))]
      (recur (conj! forms f))
      (persistent! forms))))

(defn read-forms-from-file
  [path]
  (if-not (exists? path)
    []
    (let [is (io/input-stream path)
          r1 (readers/input-stream-push-back-reader is)
          r  (readers/source-logging-push-back-reader r1 1 path)]
      (read-forms r))))

(defn read-forms-from-str
  [s]
  (let [r (readers/string-push-back-reader s)]
    (read-forms r)))

;;------------------------------------------------------------
;; CLJ/CLJS Repos
;;------------------------------------------------------------

(defn clone-or-fetch-repos
  []
  (sh "script/get-repos.sh"))

(defn get-repo-version
  [repo]
  (trim (:out (sh "git" "describe" "--tags" :dir (str repo-dir "/" repo)))))

(defn get-repo-versions
  []
  (let [sh-return (sh "script/versions.sh")
        versions (split-lines (:out sh-return))]
    versions))

(defn get-version-num
  [v]
  (Integer/parseInt (subs v 1)))

(defn get-versions-to-parse
  [latest]
  (let [versions (get-repo-versions)]
    (if-not latest
      [nil versions]
      (let [latest-num (get-version-num latest)]
        (split-with #(<= (get-version-num %) latest-num) versions)))))

(defn checkout-version!
  [v]
  (sh "script/checkout.sh" v))

(defn get-github-file-link
  [repo path [start-line end-line]]
  (let [version (get *repo-version* repo)
        strip-path (subs path (inc (count repo)))]
    (str "https://github.com/clojure/" repo "/blob/" version "/" strip-path
         "#L" start-line "-L" end-line)))

;;------------------------------------------------------------
;; Docs Repo
;;------------------------------------------------------------

(defn git-docs-repo!
  [& args]
  (apply sh "git" (concat args [:dir docs-repo-dir])))

(defn docs-repo-branch
  []
  (let [result (git-docs-repo! "rev-parse" "--abbrev-ref" "HEAD")]
    (:out result)))

(defn docs-repo-branch-exists?
  [branch]
  (let [result (git-docs-repo! "show-ref" "--verify" branch)]
    (zero? (:exit result))))

(defn prepare-docs-repo!
  []
  ;; create repo if needed
  (when-not (exists? docs-repo-dir)
    (mkdir docs-repo-dir)
    (git-docs-repo! "init"))

  ;; change to docs branch
  (when-not (= docs-branch (docs-repo-branch))
    (if (docs-repo-branch-exists? docs-branch)
      (git-docs-repo! "checkout" docs-branch)
      (git-docs-repo! "checkout" "--orphan" docs-branch))))

(defn docs-repo-has-version?
  [v]
  (let [result (git-docs-repo! "rev-parse" v)
        present? (zero? (:exit result))]
    present?))

(defn docs-repo-out-of-sync!
  [msg]
  (println "\nERROR: docs-repo out of sync with symbol-history.")
  (println "  " msg)
  (println "   You will need to delete symbol-history and docs-repo and start the tool again.")
  (System/exit 1))

(defn verify-docs-repo-sync!
  [past-versions versions]
  (doseq [version past-versions]
    (when-not (docs-repo-has-version? version)
      (docs-repo-out-of-sync! (str version " has no tag"))))
  (doseq [version versions]
    (when (docs-repo-has-version? version)
      (docs-repo-out-of-sync! (str version " already has tag")))))

(defn clear-docs-repo!
  [msg]
  (git-docs-repo! "rm" "-rf" "."))

(defn commit-docs-repo!
  [version]
  (git-docs-repo! "add" ".")
  (git-docs-repo! "commit" "-m" version)
  (git-docs-repo! "tag" version))

;;------------------------------------------------------------
;; Repo Helpers
;;------------------------------------------------------------

(defn get-repo-path
  "Get path to the given repo file"
  [ns- repo file]
  (let [path (get-in cljs-ns-paths [ns- repo file])]
    (str repo-dir "/" repo "/" path "/" file)))

(defn get-forms
  "Get forms from the given repo file"
  [ns- repo file]
  (read-forms-from-file (get-repo-path ns- repo file)))

;;------------------------------------------------------------
;; Docstring Helpers
;;------------------------------------------------------------

(defn get-docstring-indent
  [docstring]
  (let [lines (split-lines docstring)]
    (if (> (count lines) 1)
      (let [[first-line & indented-lines] lines
            get-indent-length #(count (re-find #"^ *" %))
            has-content? #(pos? (count (trim %)))]
        (->> indented-lines
             (filter has-content?)
             (map get-indent-length)
             (apply min 3)))
      0)))

(defn fix-docstring
  "Remove indentation from docstring."
  [docstring]
  (when (string? docstring)
    (let [indent-length (get-docstring-indent docstring)]
      (if (zero? indent-length)
        docstring
        (let [[first-line & indented-lines] (split-lines docstring)
              indent (re-pattern (str "^ {" indent-length "}"))
              remove-indent #(replace % indent "")]
          (->> indented-lines
               (map remove-indent)
               (cons first-line)
               (join "\n")))))))

(defn try-remove-docs
  "Try to remove docstring/attr-map from source if they are on their expected lines."
  [source {:keys [start-line end-line forms] :as expected-docs}]
  (if (nil? expected-docs)
    source
    (let [i-lines (map-indexed vector (split-lines source))
          to-str #(join "\n" (map second %))
          doc-line? #(<= start-line (first %) end-line)
          doc-str (to-str (filter doc-line? i-lines))
          actual-forms (read-forms-from-str doc-str)]
      (if (= actual-forms forms)
        (to-str (remove doc-line? i-lines))
        (do
          (binding [*out* *err*]
            (println "=====================================")
            (println "Warning: couldn't remove docstring:")
            (println "expected:" (pr-str forms))
            (println "actual:" (pr-str actual-forms))
            (println "source:" (pr-str source))
            (println "====================================="))
          source)))))

(defn try-locate-docs
  "Try to guess which lines the given docs are on (for defn/defmacro)."
  [{:keys [whole head doc sig-body] :as forms}]
  (when (seq doc)
    (let [get-line #(:line (meta %))
          first-line (get-line whole)
          before-line (or (get-line (second head))
                          (get-line (first head)))
          after-line (get-line (first sig-body))]
      (when (< before-line after-line)
        {:start-line (-> before-line inc (- first-line))
         :end-line (-> after-line dec (- first-line))
         :forms doc}))))

;;------------------------------------------------------------
;; Form Parsing
;;------------------------------------------------------------

(defn get-fn-macro
  "looks for a call of the form:
  (. (var %) (setMacro))"
  [form]
  (let [to-vec #(if (seq? %) (vec %) %)]
    (match (to-vec (map to-vec form))
      ['. ['var name-] ['setMacro]] name-
      :else nil)))

(defn get-fn-macros
  [forms]
  (set (keep get-fn-macro forms)))

(defn parse-defn-or-macro
  [form]
  (let [type- ({'defn "function" 'defmacro "macro"} (first form))
        args (drop 2 form)
        docstring (let [ds (first args)]
                    (when (string? ds)
                      ds))
        args (if docstring (rest args) args)
        attr-map (let [m (first args)]
                   (when (map? m) m))
        args (if attr-map (rest args) args)
        private? (:private attr-map)
        doc-forms (cond-> []
                    docstring (conj docstring)
                    attr-map (conj attr-map))
        signatures (if (vector? (first args))
                     (take 1 args)
                     (map first args))
        expected-docs (try-locate-docs
                        {:whole form
                         :head (take 2 form)
                         :doc doc-forms
                         :sig-body args})]
    (when-not private?
      {:expected-docs expected-docs
       :docstring (fix-docstring docstring)
       :signatures signatures
       :type type-})))

(defn parse-def-fn
  [form]
  (let [name- (second form)
        m (meta name-)
        docstring (fix-docstring (:doc m))
        signatures (when-let [arglists (:arglists m)]
                     (when (= 'quote (first arglists))
                       (second arglists)))]
    {:docstring docstring
     :signatures signatures
     :type "function"}))

(defmulti parse-form*
  (fn [form]
    (cond
      (and (= 'defn (first form))
           (not (:private (meta (second form)))))
      "defn"

      (and (= 'defmacro (first form))
           (not (:private (meta (second form)))))
      "defmacro"

      (and (= 'def (first form))
           (list? (nth form 2 nil))
           (= 'fn (first (nth form 2 nil)))
           (not (:private (meta (second form)))))
      "def fn"

      :else nil)))

(defmethod parse-form* "def fn"
  [form]
  (parse-def-fn form))

(defmethod parse-form* "defn"
  [form]
  (parse-defn-or-macro form))

(defmethod parse-form* "defmacro"
  [form]
  (parse-defn-or-macro form))

(defmethod parse-form* nil
  [form]
  nil)

(defn parse-location
  [form ns- repo]
  (let [m (meta form)
        lines [(:line m) (:end-line m)]
        num-lines (inc (- (:end-line m) (:line m)))
        source (join "\n" (take-last num-lines (split-lines (:source m))))
        filename (subs (:file m) (inc (count repo-dir)))
        github-link (get-github-file-link repo filename lines)]
    {:ns ns-
     :source source
     :filename filename
     :lines lines
     :github-link github-link}))

(defn parse-common-def
  [form ns- repo]
  (let [name- (second form)
        name-meta (meta name-)
        return-type (:tag name-meta)
        manual-macro? (or (*fn-macros* name-)
                          (:macro name-meta))]
    (merge
      {:name name-
       :full-name (str ns- "/" name-)
       :return-type return-type}

      (when manual-macro?
        {:type "macro"}))))

(defn parse-form
  [form ns- repo]
  (when-let [specific (parse-form* form)]
    (let [common (parse-common-def form ns- repo)
          location (parse-location form ns- repo)
          merged (merge specific location common)
          final (update-in merged [:source] try-remove-docs (:expected-docs specific))]
      final)))

(defn parse-special*
  "Parse cljs special forms of the form:
  (defmethod parse 'symbol ...)"
  [form]
  (when (and (list? form)
             (= 'defmethod (first form))
             (= 'parse (second form)))
    (let [quoted-name (nth form 2)
          name- (second quoted-name)]
      {:name name-})))

(defn parse-special
  [form ns- repo]
  (when-let [special (parse-special* form)]
    (let [location (parse-location form ns- repo)
          extras {:full-name (str ns- "/" (:name special))
                  :type "special form"}
          final (merge special location extras)]
      final)))

(defn parse-api
  "Parse the functions and macros from the given repo file"
  [ns- repo file]
  (println " " ns- repo file)
  (let [forms (get-forms ns- repo file)]
    (binding [*fn-macros* (get-fn-macros forms)]
      (doall (keep #(parse-form % ns- repo) forms)))))

(defn get-imported-macro-api
  [forms macro-api]
  (let [get-imports #(match % (['import-macros 'clojure.core x] :seq) x :else nil)
        macro-names (->> forms (keep get-imports) first set)]
    (filter #(macro-names (:name %)) macro-api)))

(defn get-non-excluded-macro-api
  [forms macro-api]
  (let [ns-form (first (filter #(= 'ns (first %)) forms))
        get-excludes #(match % ([:refer-clojure :exclude x] :seq) x :else nil)
        macro-names (->> ns-form (drop 2) (keep get-excludes) first set)]
    (remove #(macro-names (:name %)) macro-api)))

;;------------------------------------------------------------
;; Namespace API parsing
;;------------------------------------------------------------

(defmulti parse-ns-api (fn [ns-] ns-))

(defn parse-extra-macros-from-clj
  "cljs.core uses some macros from clojure.core, so find those here"
  []
  (let [clj-api (concat (parse-api "cljs.core" "clojure" "core.clj")
                        (parse-api "cljs.core" "clojure" "core_deftype.clj")
                        (parse-api "cljs.core" "clojure" "core_print.clj")
                        (parse-api "cljs.core" "clojure" "core_proxy.clj"))
        clj-api (filter #(= "macro" (:type %)) clj-api)
        cljs-forms   (get-forms "cljs.core" "clojurescript" "core.clj")
        imports      (get-imported-macro-api     cljs-forms clj-api)
        non-excludes (get-non-excluded-macro-api cljs-forms clj-api)]
    (println "   " (count imports) "macros imported from clojure.core")
    (println "   " (count non-excludes) "macros non-excluded clojure.core")
    (concat imports non-excludes)))

(defn parse-cljs-special-forms
  "cljs.core has some special forms defined in analyzer.clj, so find those here"
  []
  (let [ns- "cljs.core"
        repo "clojurescript"
        forms (concat (get-forms ns- repo "analyzer.clj")
                      (get-forms ns- repo "compiler.clj"))
        specials (keep #(parse-special % ns- repo) forms)]
    (println "   " (count specials) "special forms in cljs.analyzer")
    specials))

(defmethod parse-ns-api "cljs.core" [ns-]
  (let [clj-api  (parse-api ns- "clojurescript" "core.clj")
        cljs-api (parse-api ns- "clojurescript" "core.cljs")
        extra-macro-api (parse-extra-macros-from-clj)
        special-forms (parse-cljs-special-forms)
        forms (concat extra-macro-api clj-api cljs-api special-forms)]
    forms))

(defmethod parse-ns-api "cljs.test" [ns-]
  (concat (parse-api ns- "clojurescript" "test.cljs")
          (parse-api ns- "clojurescript" "test.clj")))

(defmethod parse-ns-api "cljs.reader" [ns-]
  (parse-api ns- "clojurescript" "reader.cljs"))

(defmethod parse-ns-api "clojure.set" [ns-]
  (parse-api ns- "clojurescript" "set.cljs"))

(defmethod parse-ns-api "clojure.string" [ns-]
  (parse-api ns- "clojurescript" "string.cljs"))

(defmethod parse-ns-api "clojure.walk" [ns-]
  (parse-api ns- "clojurescript" "walk.cljs"))

(defmethod parse-ns-api "clojure.zip" [ns-]
  (parse-api ns- "clojurescript" "zip.cljs"))

(defmethod parse-ns-api "clojure.data" [ns-]
  (parse-api ns- "clojurescript" "data.cljs"))

(defn parse-all
  []
  (doall (mapcat parse-ns-api (keys cljs-ns-paths))))

;;------------------------------------------------------------
;; Symbol History
;;------------------------------------------------------------

(def docs-dir "docs")
(def history-filename "symbol-history")

(defn get-symbol-history
  []
  (let [history (atom {:symbols #{} :version-map {}})]
    (if-not (exists? history-filename)
      [nil history]
      (let [[latest & lines] (split-lines (slurp history-filename))]
        (doseq [line lines]
          (let [[name- & versions] (split line #"\s+")]
            (swap! history assoc-in [:version-map name-] (vec versions))
            (let [c (first (last versions))]
              (when (= \+ c)
                (swap! history update-in [:symbols] conj name-)))))
        [latest history]))))

(defn make-history-line
  [name- versions pad]
  (let [name- (format (str "%-" pad "s") name-)]
    (join " " (concat [name-] versions))))

(defn make-history-lines
  [version-map]
  (let [names (keys version-map)
        pad (->> names (map count) (apply max) (+ 2))]
    (for [[name- versions] (sort-by first version-map)]
      (make-history-line name- versions pad))))

(defn write-history!
  [version-map latest]
  (let [table (join "\n" (make-history-lines version-map))
        version-and-table (str latest "\n" table)]
    (spit (str *output-dir* "/" history-filename) table)
    (spit history-filename version-and-table)))

(defn mark-symbol-added!
  [history version s]
  (let [v-change (str "+" version)]
    (if-not (contains? (:version-map @history) s)
      (swap! history assoc-in [:version-map s] [v-change])
      (when-not (first (filter #(= % v-change) (get-in @history [:version-map s])))
        (swap! history update-in [:version-map s] conj v-change)))))

(defn mark-symbol-removed!
  [history version s]
  (let [v-change (str "-" version)]
    (swap! history update-in [:version-map s] conj v-change)))

(defn update-history!
  [history version symbols]
  (let [[added removed _] (diff symbols (:symbols @history))]
    (doseq [s added]
      (mark-symbol-added! history version s))
    (doseq [s removed]
      (mark-symbol-removed! history version s))
    (swap! history assoc :symbols symbols)
    (write-history! (:version-map @history) version)))

(defn attach-history
  [item version-map]
  (let [name- (:full-name item)
        history (get version-map name-)]
    (assoc item :history history)))

(defn attach-history-to-items
  [items version-map]
  (map #(attach-history % version-map) items))

;;------------------------------------------------------------
;; Doc file writing
;;------------------------------------------------------------

(defn symbol->filename
  [s]
  (-> (name s)
      (replace "." "DOT")
      (replace ">" "GT")
      (replace "<" "LT")
      (replace "!" "BANG")
      (replace "?" "QMARK")
      (replace "*" "STAR")
      (replace "+" "PLUS")
      (replace "=" "EQ")
      (replace "/" "SLASH")))

(defn item-filename
  [item]
  (str *output-dir* "/" docs-dir "/" (:ns item) "_" (symbol->filename (:name item))))

(defn make-history-text
  [text]
  (let [plus-or-minus (first text)
        change (if (= \+ plus-or-minus) "Added" "Removed")
        version (subs text 1)]
    (str change " in " version)))

(defn cljsdoc-section
  [title content]
  (when content
    (str "===== " title "\n" content "\n")))

(defn make-cljsdoc
  [item]
  (join "\n"
    (keep identity
      [(cljsdoc-section "Name" (:full-name item))
       (cljsdoc-section "Type" (:type item))
       (cljsdoc-section "Return Type" (:return-type item))
       (cljsdoc-section "Docstring" (:docstring item))
       (cljsdoc-section "Signature" (join "\n" (:signatures item)))
       (cljsdoc-section "Filename" (:filename item))
       (cljsdoc-section "Source" (:source item))
       (cljsdoc-section "Github" (:github-link item))
       (cljsdoc-section "History" (join "\n" (map make-history-text (:history item))))
       ""])))

(defn dump-doc-file!
  [item]
  (let [filename (str (item-filename item) ".cljsdoc")
        cljsdoc-content (make-cljsdoc item)]
    (spit filename cljsdoc-content :append true)))

(defn dump-api-docs!
  [api]
  (doseq [item api]
    (dump-doc-file! item)))

;;------------------------------------------------------------
;; Program Entry
;;------------------------------------------------------------

(defn -main
  []

  ;; HACK: We need to create this so 'tools.reader' doesn't crash on `::ana/numeric`
  ;; which is used by cljs.core. (the ana namespace has to exist)
  (create-ns 'ana)

  (println "\nCloning or updating repos...")
  (clone-or-fetch-repos)

  (prepare-docs-repo!)

  (let [[latest history] (get-symbol-history)
        [past-versions versions] (get-versions-to-parse latest)]

    (println "\nVerifying docs-repo is in sync with symbol-history...")
    (verify-docs-repo-sync! past-versions versions)

    (doseq [version versions]

      (println "\n=========================================================")
      (println "\nChecking out" version "...")
      (checkout-version! version)

      (binding [*output-dir* docs-repo-dir
                *repo-version* {"clojurescript" version
                                "clojure" (get-repo-version "clojure")}]

        (println "\nParsing...")
        (let [parsed (parse-all)
              symbols (set (map :full-name parsed))]

          (clear-docs-repo! version)

          (println "\nWriting updated history to" history-filename "...")
          (mkdir *output-dir*)
          (update-history! history version symbols)

          (println "\nWriting docs to" *output-dir*)
          (mkdir (str *output-dir* "/" docs-dir))
          (let [parsed (attach-history-to-items parsed (:version-map @history))]
            (dump-api-docs! parsed))

          (println "\nCommitting docs at tag" version "...")
          (commit-docs-repo! version))

        (println "\nDone."))))

  ;; have to do this because `sh` leaves futures hanging,
  ;; preventing exit, so we must do it manually.
  (System/exit 0))

