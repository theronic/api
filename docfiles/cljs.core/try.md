---
name: cljs.core/try
see also:
  - cljs.core/catch
  - cljs.core/finally
  - cljs.core/throw
---

## Summary

## Details

The expressions (`expr*`) are evaluated and, if no exceptions occur, the value
of the last is returned.

If an exception occurs and catch clauses (`catch-clause*`) are provided, each is
examined in turn and the first for which the thrown exception is an instance of
the named class is considered a matching catch clause. If there is a matching
catch clause, its expressions are evaluated in a context in which name is bound
to the thrown exception, and the value of the last is the return value of the
function.

If there is no matching catch clause, the exception propagates out of the
function. Before returning, normally or abnormally, any `finally-clause?`
expressions will be evaluated for their side effects.

`try` is one of ClojureScript's [special forms](http://clojure.org/special_forms).

## Examples
