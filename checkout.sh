#!/bin/sh

set -e

CLJS_VERSION="$1"

if [ -z "$CLJS_VERSION" ]; then
  echo ""
  echo "USAGE: checkout <version>"
  echo ""
  echo "This will checkout clojurescript to the given version,"
  echo "and clojure to the appropriate matching version."
  echo ""
  echo "Choose from the following versions:"
  cd repos/clojurescript
  git tag | grep "^r" | sort -n -k1.2 | column
  exit 1
fi

echo "Checking out ClojureScript $CLJS_VERSION..."
pushd repos/clojurescript
git checkout -- .
git checkout $CLJS_VERSION
git clean -xdf

cd script
CLJ_VERSION=`sed -n -e 's/^CLOJURE_RELEASE="\(.*\)"/\1/p' bootstrap`
if [ -z "$CLJ_VERSION" ]; then
  CLJ_VERSION=`sed -n -e 's/^unzip .*clojure-\(.*\)\.zip/\1/p' bootstrap`
fi
if [ -z "$CLJ_VERSION" ]; then
  echo "Could not find clojure version to checkout"
  exit 1
fi

echo "Checking out Clojure $CLJ_VERSION..."
popd
cd repos/clojure
git checkout -- .
git checkout "clojure-$CLJ_VERSION"
git clean -xdf