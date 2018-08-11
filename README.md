# scalaz-config

[![Gitter](https://badges.gitter.im/scalaz/scalaz-config.svg)](https://gitter.im/scalaz/scalaz-config?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)


### Goal

The goal is to implement a principled type-safe FP library that enables reading and composing application configuration parameters, while returning maximum errors along with details on stale and extraneous parameters. The project will also aim at re-generating configuration files back from the ADTs

### Introduction & Highlights

There are three main components to the configuration library: User API, reporting, and backend API.

The User API provides a functional, simple interface to the configuration. The intention is to make the API typesafe, simple, and expressive.

The Backend functionality implements the logic to read the configuration from its serialized form. This should ultimately utilize functional abstractions like the Reader Monad or ZIO to provide a consistent method for reading in configured values.

The Reporting functionality provides reporting on how configuration values are actually used, and what is unused. This also implies returning errors if user accidentally input any extraneous parameters compared to the target ADTs.

* Fast reading of configured values from a variety of formats into user defined types in a composable manner.
* Maximum error accumulation.
* Stands against throwing errors in any way.
* Reports on stale configurations, error on extraneous configuration parameters.
* Support configuration existing as HOCON and in sys environment.
* Library aims at **not** having proliferated usage of compile time/run-time reflection.
* Emit configuration properties from the user defined types.
* Above all, absolutely simple to use and seamless interoperability with existing functional libraries in scala world.

### Competitions

#### Typesafe Config
* Type safe, pure FP? ✘ (Exceptions are thrown, and not with nice error messages)
* Scalaz compatible ? ✘
* No macros/reflection? ✔
* Property file parsing ? ✔
* Find the extraneous parameters in a config file ✘
* Better error Reporting ✘
* Reverse direction inspection.✔
* Hocon support? ✔
* No Inheritance complexity in hocon ? ✘

#### Clear Config
* Type safe, pure FP? ✔
* Throws no error? ✘
* Depends on Typesafe config? ✘ (flattened configuration parameters)
* No macros/reflection ? ✔
* Scalaz Compatible ? ✔
* Property file parsing ? ✔
* Better error Reporting ? ✔
* Reverse direction inspection ? ✘
* Find the extraneous parametres in a config file ✘/✔

#### Pure Config
* Type safe, pure FP? ✘/✔
* Throws no error? ✘
* Depends on Typesafe config? ✔
* No macros/reflection ? ✘
* Scalaz Compatible ? ✘ (Integration with cats, http4s, fs2 etc available)
* Property file parsing/HOCON ? ✔
* Better error Reporting ? ✘
* Reverse direction inspection ? ✘
* Find the extraneous parametres in a config file ✘

Note: Experienced compile time stack error using the library.

#### Cedi Config
* Type safe, pure FP? ✘/✔ (Exposes type safe library operations that can be unsafe)
* Throws no error? ✘
* Depends on Typesafe config? ✔
* No macros/reflection ? ✘
* Scalaz Compatible ? ✘/✔
* No macros/reflection ? ✘
* Property file parsing/HOCON ? ✔
* Reverse direction inspection ? ✘
* Find the extraneous parametres in a config file ✘

#### Background
It is important to learn Reader Monad, Applicatives as we have seen quite a lot of talks in FP world on config parsing using these concepts.  I’d like to explore if it makes sense to use ZIO as well.

ZIO:
* https://github.com/scalaz/scalaz-zio/blob/master/README.md

Reader Monad:
* http://hackage.haskell.org/package/mtl-2.2.2/docs/Control-Monad-Reader.html

Applicatives:
* http://hackage.haskell.org/package/base-4.11.1.0/docs/Control-Applicative.html

Projects that we can get inspired:

* https://github.com/japgolly/clear-config
* http://www.haskellforall.com/2016/12/dhall-non-turing-complete-configuration.html?m=1

#### A few other projects and gists that can be useful.

* https://github.com/ajevans85/scala-simple-env-config

Finding the tree structure of config parameters from case class (shapeless)
* https://gist.github.com/afsalthaj/2e019e81e22a89f6dc49047e0cefd30e
