---
name: cljs.core/set-validator!
see also:
  - cljs.core/atom
  - cljs.core/get-validator
---

## Summary

## Details

Sets a validator function for atom `a`.

`fn` must be nil or a side-effect-free function of one argument, which will be
passed the intended new state on any state change. `fn` should return false or
throw an Error if the new state is unacceptable.

If the current value of `a` is unacceptable to `fn` when `set-validator!` is
called, an Error will be thrown and the validator will not be set.

`(set-validator! my-atom nil)` will remove the validator from `my-atom`.

## Examples
