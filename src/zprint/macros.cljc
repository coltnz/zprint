(ns zprint.macros
  (:require [clojure.string :as s]))

(defmacro dbg-pr
  "Output debugging print with pr."
  [options & rest]
  `(when (:dbg? ~options) (println (:dbg-indent ~options) (pr-str ~@rest))))

(defmacro dbg
  "Output debugging print with println."
  [options & rest]
  `(when (:dbg? ~options) (println (:dbg-indent ~options) ~@rest)))

(defmacro dbg-form
  "Output debugging print with println, and always return value."
  [options id form]
  `(let [value# ~form]
     (when (:dbg? ~options)
       (println (:dbg-indent ~options) ~id (pr-str value#)))
     value#))

(defmacro dbg-print
  "Output debugging print with println."
  [options & rest]
  `(when (or (:dbg? ~options) (:dbg-print? ~options))
     (println (:dbg-indent ~options) ~@rest)))

(defmacro zfuture
  "Takes an option map and a body of expressions.  If it
  is possible to use futures (i.e., we are in Clojure)
  then examine the options map for :parallel? and if true
  use futures.  If not, just return the value.  Note well
  that the returns from this are wildly different if a future
  is used or if a future is not used, but there is no way
  around that.  Of which I'm aware, anyway."
  [options & body]
  #?(:clj `(if (:parallel? ~options) (future ~@body) (do ~@body))
     :cljs `(do ~@body)))
