<h1 align="center">Unsplash Images</h1>



<p align="center">
📷🖼 View all images of interest from Unsplash
</p>
</br>

<p align="center">
<img src="https://github.com/J-cart/Unsplash_Images/assets/82452881/b20d2b0a-0774-4964-92b6-19495b38d507" width="280"/>
<img src="https://github.com/J-cart/Unsplash_Images/assets/82452881/24da88aa-e30a-4319-997d-c2cfd480f58c" width="280"/>
<img src="https://github.com/J-cart/Unsplash_Images/assets/82452881/1b355680-af69-4928-bac6-46e93e4c9aa2" width="252"/>
</p>


## Demo
   .....
## Download
 Coming Soon .....
<img src="/previews/preview.gif" align="right" width="320"/>

## Tech stack & Open-source libraries
- Minimum SDK level 24
- [Kotlin](https://kotlinlang.org/) based, [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/) for asynchronous.
- Jetpack
  - Lifecycle: Observe Android lifecycles and handle UI states upon the lifecycle changes.
  - ViewModel: Manages UI-related data holder and lifecycle aware. Allows data to survive configuration changes such as screen rotations.
  - View Binding: Binds UI components in your layouts to data sources in your app using a declarative format rather than programmatically.
  - Room: Constructs Database by providing an abstraction layer over SQLite to allow fluent database access.
  - [Hilt](https://dagger.dev/hilt/): for dependency injection.
- Architecture
  - MVVM Architecture (View - View Binding - ViewModel - Model)
  - Repository Pattern
- [Retrofit2 & OkHttp3](https://github.com/square/retrofit): Construct the REST APIs and paging network data.
- [Moshi](https://github.com/square/moshi/): A modern JSON library for Kotlin and Java.
- [Gson](https://mvnrepository.com/artifact/com.google.code.gson/gson): Gson is a Java library that can be used to convert Java Objects into their JSON representation. It can also be used to convert a JSON string to an equivalent Java object.
- [ksp](https://github.com/google/ksp): Kotlin Symbol Processing API.
- [Material-Components](https://github.com/material-components/material-components-android): Material design components for building ripple animation, and CardView.
- [Paging Library](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) and [Remote Mediator](https://developer.android.com/topic/libraries/architecture/paging/v3-network-db): The Paging library helps you load and display pages of data from a larger dataset from local storage or over network. This approach allows your app to use both network bandwidth and system resources more efficiently.
- [Coil](https://github.com/coil-kt/coil): Loading images from network.
- **API** 
  - [Unsplash Api](https://unsplash.com/documentation): Official Unsplash API. Create with the largest open collection of high-quality photos.

## Architecture
**Unsplash Images** is based on the MVVM architecture and the Repository pattern, which follows the [Google's official architecture guidance](https://developer.android.com/topic/architecture).

The overrall architecture of **Unsplash Images** is composed of two layers; the UI layer and the data layer. Each layer has dedicated components and they have each different responsibilities, as defined below:

**Unsplash Images** was built with [Guide to app architecture](https://developer.android.com/topic/architecture), so it would be a great sample to show how the architecture works in real-world projects.


### Architecture Overview

- Each layer follows [unidirectional event/data flow](https://developer.android.com/topic/architecture/ui-layer#udf); the UI layer emits user events to the data layer, and the data layer exposes data as a stream to other layers.
- The data layer is designed to work independently from other layers and must be pure, which means it doesn't have any dependencies on the other layers.

With this loosely coupled architecture, you can increase the reusability of components and scalability of your app.

### UI Layer

The UI layer consists of UI elements to configure screens that could interact with users and [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) that holds app states and restores data when configuration changes.
- UI elements observe the data flow via [View Binding](https://developer.android.com/topic/libraries/view-binding), which is the most essential part of the MVVM architecture. 

### Data Layer

The data Layer consists of repositories, which include business logic, such as querying data from the local database and requesting remote data from the network. It is implemented as an offline-first source of business logic and follows the [single source of truth](https://en.wikipedia.org/wiki/Single_source_of_truth) principle.<br>

**Unsplash Images** is an offline functional app that is able to perform some all of its core functionality without access to the internet. 
 Users might need to be updated on network resources to get up to date info and access some other functionalities too. It relies heavily on the remote mediator of the paging library ![paging3-base-lifecycle](https://user-images.githubusercontent.com/82452881/201220135-b40390a3-268b-4a2c-b5c6-a4dbec009c4e.png)
 For further information, you can check out [Page from network and database](https://developer.android.com/topic/libraries/architecture/paging/v3-network-db).





