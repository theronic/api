(ns cljs-api-gen.util
  (:refer-clojure :exclude [replace])
  (:require
    [clojure.string :refer [replace]]
    ))

(defn mapmap
  "Apply a map function over the values of a map, returning a map."
  [mapf datamap]
  (let [[ks vs] ((juxt keys vals) datamap)
        result (zipmap ks (map mapf vs))]
    result))

(defn split-ns-and-name
  [s]
  ((juxt namespace name) (symbol s)))

