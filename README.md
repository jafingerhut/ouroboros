# Introduction

A tiny test project that demonstrates an odd behavior of the
ClojureScript compiler that I am hoping to figure out whether it is a
bug, expected behavior, lucky accident, or what.

My motivation for asking is because I also get the same warning when
compiling the latest version of the ClojureScript version of the
core.rrb-vector library, version 0.1.0, from which the code in this
project was boiled down to the core of the issue.  While it is trivial
to change the code in this project to avoid the warning, it is not so
trivial to do so for the current core.rrb-vector code (possible, but
perhaps a bit tedious to restructure the code to avoid the warning).
This is an open issue on that library to track what I learn about it:
https://clojure.atlassian.net/browse/CRRBV-24

Namespace `ouroboros.foo` is normal enough.  It defines a couple of
`deftype`s, and some functions for creating instances of those types.
It requires namespace `ouroboros.bar` and calls a function from there
called `tail-offset`.

Namespace `ouroboros.bar` is where the strangeness is.  It checks
whether its argument is of type `ouroboros.foo/Transient`, one of the
two types defined in `ouroboros.foo`, but it does not require
`ouroboros.foo`.

There are warnings from the ClojureScript compiler while compiling
this code, which makes reasonable sense why they would appear:

```
WARNING: Use of undeclared Var ouroboros.foo/Transient at line 5 src/main/cljs/ouroboros/bar.cljs
WARNING: Use of undeclared Var ouroboros.foo/Transient at line 10 src/main/cljs/ouroboros/bar.cljs
```

However, when I run tests with this code, which exercises calling the
function `tail-offset`, once with an argument that is of type
`ouroboros.foo/Transient`, once with the other type, everything seems
to work functionally at run time as one might hope.

Note: The tests pass when I compile the ClojureScript using the
`:advanced` level of optimization.  I get various kinds of failures
for these other settings of `:optimizations`:

* `:none` causes cljsbuild test to fail with error: `ReferenceError:
  goog is not defined`

* `:whitespace` causes cljsbuild test to fail with error: `TypeError:
  Cannot set property 'Unicode' of undefined`

* `:simple` causes cljsbuild test to fail with error: `ReferenceError:
  ouroboros is not defined`

* `:advanced` causes cljsbuild test to pass with no errors, only the
  warning messages below during compilation, which appear for all
  settings of `:optimizations`:

```
WARNING: Use of undeclared Var ouroboros.foo/Transient at line 5 src/main/cljs/ouroboros/bar.cljs
WARNING: Use of undeclared Var ouroboros.foo/Transient at line 10 src/main/cljs/ouroboros/bar.cljs
```

Why doesn't the compiler give an error?

How is this code working?  Or is it some strange happenstance accident
that it only seems to work, and could break at any moment with the
next ClojureScript compiler release?

Run tests with both Node.js and Spidermonkey JavaScript runtimes:
```bash
$ lein with-profile +cljs cljsbuild test
```

Run tests with only Node.js:
```bash
$ lein with-profile +cljs cljsbuild test node
```

Run tests with only Spidermonkey (assumed to be installed as command
named `js52`):
```bash
$ lein with-profile +cljs cljsbuild test spidermonkey
```


# Sample output

This is the output I get when running the test command that runs the
tests with both JavaScript runtimes:

```
$ lein with-profile +cljs cljsbuild test
Compiling ClojureScript...
Compiling ["out/test.js"] from ["src/main/cljs" "src/test/cljs"]...
WARNING: Use of undeclared Var ouroboros.foo/Transient at line 5 src/main/cljs/ouroboros/bar.cljs
WARNING: Use of undeclared Var ouroboros.foo/Transient at line 10 src/main/cljs/ouroboros/bar.cljs
[32mSuccessfully compiled ["out/test.js"] in 11.54 seconds.[0m
Running ClojureScript test: node
Running Basic Tests

Testing ouroboros.test-cljs

Testing ouroboros.test-foo
got here #1
dbg tail-offset type= #object[Mp]  val= false  (.-tail vec)= 2
dbg tail-offset type= #object[Lp]  val= true  tidx= 6
got here #2

Ran 1 tests containing 1 assertions.
0 failures, 0 errors.
Running ClojureScript test: spidermonkey
Running Basic Tests

Testing ouroboros.test-cljs

Testing ouroboros.test-foo
got here #1
dbg tail-offset type= #object[Mp]  val= false  (.-tail vec)= 2
dbg tail-offset type= #object[Lp]  val= true  tidx= 6
got here #2

Ran 1 tests containing 1 assertions.
0 failures, 0 errors.
```


## License

Copyright © 2019 Andy Fingerhut

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.
