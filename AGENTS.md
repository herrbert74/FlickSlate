# AI Agent Development Guidelines

This document provides a set of rules and guidelines for the AI agent to follow when contributing to the FlickSlate project. The primary goal is to maintain code quality, consistency, and alignment with modern Kotlin development practices.

## Core Principles

- **Kotlin First**: The project is written in Kotlin. The agent must produce idiomatic Kotlin code.
- **Modern Android Development**: Adhere to the latest Android development best practices, including Jetpack Compose, Coroutines, and a well-defined architecture.
- **Dependency Awareness**: Be mindful of the existing project structure and dependencies.
- Use **tabs** instead of spaces.

---

## Project structure

* **main** - Contains the entry points to the application, plus Jetpack **navigation**.
* **feature** modules (and submodules for **domain**, **data**, and **ui**):
    * **account**
    * **movies**
    * **tv**
    * **search**
* **Submodules** within above features.
    * **domain** - Contains the **shared** **domain model**, the **api interface**, and optionally **use cases**. Domain depends only on itself and all interaction it does is via _dependency
      inversion_.
    * **data** - Contains the (**db**, **network**, etc) modules.
    * **ui** - Presentation layer
* **shared** - **Shared** domain, UI and data modules specific to this app.
* **base** - Kotlin and Android base classes, reusable in any apps. 

## Rule: Naming

Add suffixes to designate types and prefixes for designate the features.

### Data classes

* Retrofit interfaces have the suffix Service.
* Room interfaces have the suffix Dao, databases the suffix DataBase.
* DataSources using the above have the suffixes RemoteDataSource and LocalDataSource.
* The DataSource interfaces have the suffix DataSource, and nested within them are the interfaces Local and Remote.
* The Repository interfaces using the DataSources have the suffix Repository, while their implementations have the suffix Accessor.

### Presentation classes

Top level composables that represent a screen have the Screen suffix.

## Rule: Prioritize Kotlin Libraries Over Java Equivalents

When a task requires adding a new library or using a library for a common problem (like JSON serialization, HTTP requests, etc.), **always prefer the Kotlin-first or Kotlin-native equivalent if one exists.**

Avoid using traditional Java libraries when a modern Kotlin alternative is available and widely adopted.

### Rationale

1.  **Idiomatic Code**: Kotlin-native libraries are designed with Kotlin's features in mind, such as coroutines for non-blocking I/O, extension functions for cleaner APIs, and null safety. This leads to more concise and readable code.
2.  **Performance**: Many Kotlin libraries are built from the ground up to be lightweight and avoid reflection (e.g., `kotlinx.serialization`), which can improve performance.
3.  **Interoperability**: While Kotlin has excellent Java interoperability, using a pure Kotlin stack reduces potential friction and simplifies the mental model for developers.

### Examples

| Instead of this (Java Library)    | Use this (Kotlin Equivalent)                             | Reason                                                           |
|:----------------------------------|:---------------------------------------------------------|:-----------------------------------------------------------------|
| **Gson** or **Jackson**           | `kotlinx.serialization`                                  | Official Kotlin library, avoids reflection, compile-time safety. |
| **RxJava**                        | **Kotlin Coroutines** (`Dispatchers`, `async`, `launch`) | The standard for asynchronous programming in modern Kotlin.      |
| **Mockito**                       | **MockK**                                                | Idiomatic mocking library designed specifically for Kotlin.      |

### Exceptions

Due to their convenience some Java libraries are still used, for example Dagger/Hilt, Retrofit.

### How to Apply this Rule

Before adding a dependency or writing code that relies on a common Java library, perform the following check:

1.  Identify the core functionality needed (e.g., "JSON parsing", "HTTP client", "mocking").
2.  Search for "Kotlin [functionality]" or "[Java Library] Kotlin alternative".
3.  Evaluate the Kotlin-first option for maturity, community adoption, and suitability for the project.
4.  Default to the Kotlin-native choice unless there is a compelling, documented reason not to.

## Rule: Make everything internal if possible

In the data layer almost everything is internal, with the exception of Repository interfaces.
This requires som tricks for dependency injection. When we want to override Dagger modules in the app module,
we need to remove the AutoBind annotation, create an internal module with a named binding, and use this internal binding to create a public binding.

It will look like this:

@Module(includes = [InternalTvRepositoryModule::class])
@InstallIn(ViewModelComponent::class)
interface TvRepositoryModule {

  @Binds
  fun bindTvRepository(@Named("Internal") impl: TvRepository): TvRepository

}

@Module
@InstallIn(ViewModelComponent::class)
internal interface InternalTvRepositoryModule {

  @Binds
  @Named("Internal")
  fun bindTvRepository(impl: TvAccessor): TvRepository

}

## Rule: Use kotlin-result library

Due to the Result class in the standard library is not a full implementation we need to use this library.
Due to problems with inline value classes, we cannot use the 2.0 version of this library.
This means that for example we cannot use isOk() as a function, but only the old isOk value.
