# FlickSlate

An Android app using the [TMDB API](https://developer.themoviedb.org/docs/getting-started).

The goal of FlickSlate is to showcase best practices and learn some new techniques.

It also showcases some of the techniques used in my articles about [caching](https://herrbert74.github.io/posts/caching-strategies-in-android/) and [architecture](https://herrbert74.github.io/posts/architecture-related-decisions-introduction/)

## 📚 Tech stack

- Application entirely written in [Kotlin](https://kotlinlang.org)
- UI developed in [Jetpack Compose](https://developer.android.com/jetpack/androidx/releases/compose)
- Following the [Material 3](https://m3.material.io/) guidelines
- Asynchronous processing using [Coroutines](https://kotlin.github.io/kotlinx.coroutines/)
- Dependency injection with [Dagger](https://github.com/google/dagger) and [Hilt](https://dagger.dev/hilt/)
- Database using [realm-kotlin](https://github.com/realm/realm-kotlin)

## 🏛 Architecture

FlickSlate architecture is Clean(ish) Architecture as [recommended by Google](https://developer.android.com/topic/architecture).

Let's take a look in each major part of the application:

* **main** - Contains the entry points to the application, plus Jetpack ***navigation**.
* **feature** modules (and submodules for **domain**, **data**, **repository**, and **ui**):
  * **movies**
  * **tv**
  * **search**
* **shared** - Contains the shared parts. Notice that some parts can be both **feature** specific or **layer** specific, as required.
  * **domain** - Contains the **shared** **domain model**, the **api interface**, and optionally **use cases**. Domain depends only on itself and all interaction it does is via _dependency
    inversion_.
  * **data** - The module containing the **shared** data for the app. Data modules contain the (**db**, **network**, etc) modules.
  * **compose** - Contains shared modules related to compose ui: the **component**, the **design** and the **navigation**.
  * **async**, the **testhelper** and the **util** modules.

In my view this reflects the optimal structure of a **medium small** app (5-10 KLOC, 3-5 features).

## 👀 Others

* Network calls are using caching strategies and fetcher functions as detailed in my [Caching Strategies in Android ](https://herrbert74.github.io/posts/caching-strategies-in-android) article.
* Many calls are doing paging and caching at the same time. It uses custom paging, so **do not** use this in production **yet**.

## 💩 Known problems

* Paging might have subtle bugs due to the shortcomings of the TMDB API:
  * It returns the cached versions of the pages, which are retained for a day, but regenerated at different times.
  * As a result, it can happen that two movies swap pages, but because only one of them is updated, one movie will be duplicated, the other will disappear.
  * I haven't tested it, but this could break paging, where we scroll to the end of a long list, but because more than the threshold number of movies are missing, the fetch is not triggered.
  * Similar problems are unresolved and unanswered on the TMDB forums, so I cannot be bothered to file an issue.
  * The API doesn't respect the no-store header either.

## 	🚧 Under construction

* Modularisation
* New features (TBD)
* Improved test coverage
* CI through Github Actions

## 📃 License

```
Copyright 2024 Zsolt Bertalan

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