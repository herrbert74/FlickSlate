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

* **presentation** - Contains the **ui**, the **design** and the **navigation**. 
* **domain** - Contains the **domain model** and the **api interface** It depends only on itself and all interaction it does is via _dependency
  inversion_.
* **data** - The module containing the data (**db**, **network**) from the app. Also holds the **repository** implementation.

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