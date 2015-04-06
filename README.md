# memoasync

Memoize operations that return `core.async` channels.

## The problem

Clojure has a standard library function [`memoize`](https://clojuredocs.org/clojure.core/memoize) function that remembers the result of a function and on subsequent calls returns the same value so it doesn't have to be calculated again.  This is a handy optimisation for calls which are relatively expensive, and where the value is not expected to change for the same parameters.  The trade-off for this optimisation is higher memory use.

The one drawback with the standard `memoize` is that it remembers everything.  So the larger the set of arguments, the more memory is required.  In a worst-case scenario there will be an infinitely large number of distinct arguments, and all available memory will be consumed.  `memoize` should not be used for such functions.  There is a different project that takes a more sophisticated approach: [`core.memoize`](https://github.com/clojure/core.memoize), this allows the developer to control the cache strategy.

### `core.async` channels

Neither of those two approaches are suitable for caching async functions that return data using a `core.async` channel.  Such channels can only be read from once.

However the type of function that returns a `core.async` channel are often those which would benefit most from local caching (e.g. loading external resources via non-blocking means).

## The solution

`memoasync` allows functions that return `core.async` channels to be memoised and different caching techniques to be used.

[`core.cache`](https://github.com/clojure/core.cache) is used internally, providing the cache.

Upon the first call the underlying function is called, the returned channel is syphoned to retrieve the value, which is put in a cache.  Subsequent calls return channels into which the cached value is put.

### What is the suitable for?

Any non-blocking function that returns a `core.async` channel that will contain a single return value.

### What is this not suitable for?

Functions that return channels that contain more than a single value.

### Example(s):

```clojure
(def cached-f (lru non-cached-f :threshold 128))
```

Memoizes `non-cached-f` backed by a LRU cache containing at most 128 items.

## License

```
Copyright 2015 Ben Ashford

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
