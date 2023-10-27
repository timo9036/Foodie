<p align="center">
<img src="previews/chef.png" width="15%"/>
<h1 align="center">🍲 Foodie - Food Recipe App 🍲</h1>
<p align="center">
Explore hundreds of delicious recipes at your fingertips!
</p>
</p>

Foodie is an app designed for food lovers who enjoy exploring new recipes and culinary adventures. Whether you're looking for a classic dish or something new and exciting, Foodie has got you covered.

![Profile views](https://komarev.com/ghpvc/?username=timo9036&color=green)
![GitHub release (latest by date)](https://img.shields.io/github/v/release/timo9036/Foodie)
[![LinkedIn](https://img.shields.io/badge/-LinkedIn-blue?style=flat-square&logo=linkedin)](https://www.linkedin.com/in/timothysliu/)
[![Email](https://img.shields.io/badge/-Email-orange?style=flat-square&logo=gmail)](mailto:timo9036@hotmail.com)

## Table of Contents

1. [Screenshots](#-screenshots)
2. [Features](#-features)
3. [Built With](#-built-with)
4. [Architecture](#️-architecture)
5. [Package Structure](#-package-structure)
6. [Installation](#️-installation)
7. [Contributing](#-contributing)
8. [License](#-license)

## 📸 Screenshots

Explore the app through these stunning visuals!

<table>
  <tr>
     <th>Main Page</th>
     <th>Detail Page</th>
     <th>Search Page</th>
  </tr>

  <tr>
    <td>
           <img src="previews/main.jpg" width="400" height="500" alt="Main Page">
   </td>
   <td>
           <img src="previews/details.jpg" width="400" height="500" alt="Detail Page">
   </td>
   <td>
           <img src="previews/search.jpg" width="400" height="500" alt="Search Page">
   </td>
  </tr>

 </table>

 <table>
  <tr>
    <th>Favorite Page</th>
    <th>ChefGPT Page</th>
    <th>About Page</th>
  </tr>

  <tr>
   <td>
           <img src="previews/favorites.jpg" width="400" height="500" alt="Favorite Page">
   </td>
   <td>
           <img src="previews/chefgpt.jpg" width="400" height="500" alt="ChefGPT Page">
   </td>
   <td>
           <img src="previews/about.jpg" width="400" height="500" alt="About Page">
   </td>
  </tr>

 </table>

## 🌟 Features

- **Instant Notifications:** Stay updated with the latest culinary delights and app updates through Firebase Cloud Messaging.
- **Explore Recipes:** Browse through a vast collection of recipes.
- **Details & Ingredients:** Get detailed instructions and ingredients list for each recipe.
- **Search Functionality:** Find exactly what you're craving with the search feature.
- **Favorites:** Save your favorite recipes for easy access later.
- **ChefGPT:** Get creative with our AI-powered recipe suggestions, with Chat, Image generation, TTS/SST.
- **Learn About Us:** Discover more about the creators and our mission on the About page.

## 🛠 Built With

<p align="center">
Explore the technology stack that powers Foodie - the recipe app for food enthusiasts!
</p>


- [Kotlin](https://kotlinlang.org/) - First class and official programming language for Android development.
- [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - For asynchronous and more..
- [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-flow/) - A cold asynchronous data stream that sequentially emits values and completes normally or with an exception.
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture) - Collection of libraries that help you design robust, testable, and maintainable apps.
  - [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - Data objects that notify views when the underlying database changes.
  - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Stores UI-related data that isn't destroyed on UI changes. 
  - [ViewBinding](https://developer.android.com/topic/libraries/view-binding) - Generates a binding class for each XML layout file present in that module and allows you to more easily write code that interacts with views.
  - [Room](https://developer.android.com/topic/libraries/architecture/room) - SQLite object mapping library.
  - [DataStore Preferences](https://developer.android.com/topic/libraries/architecture/datastore) - Jetpack DataStore is a data storage solution that allows you to store key-value pairs or typed objects with protocol buffers.
  - [Navigation Component](https://developer.android.com/guide/navigation/navigation-getting-started) - Navigation occurs between your app's destinations—that is, anywhere in your app to which users can navigate. These destinations are connected via actions.
- [Dependency Injection](https://developer.android.com/training/dependency-injection) - 
  - [Hilt-Dagger](https://dagger.dev/hilt/) - Standard library to incorporate Dagger dependency injection into an Android application.
  - [Hilt-ViewModel](https://developer.android.com/training/dependency-injection/hilt-jetpack) - DI for injecting `ViewModel`.
- [Retrofit](https://square.github.io/retrofit/) - A type-safe HTTP client for Android and Java.
- [okhttp-logging-interceptor](https://github.com/square/okhttp/blob/master/okhttp-logging-interceptor/README.md) - logging HTTP request related data.
- [Gson](https://github.com/google/gson) - Gson is a Java library that can be used to convert Java Objects into their JSON representation
- [Coil-kt](https://coil-kt.github.io/coil/) - An image loading library for Android backed by Kotlin Coroutines.
- [Material Components for Android](https://github.com/material-components/material-components-android) - Modular and customizable Material Design UI components for Android.
- [Menu - Contextual Menu](https://developer.android.com/guide/topics/ui/menus)- Menus are a common user interface component in many types of applications. 
- [Shimmer Effect](https://github.com/omtodkar/ShimmerRecyclerView) - ShimmerRecyclerView is an custom RecyclerView library based on Facebook's Shimmer effect for Android library
- [Gradle Kotlin DSL](https://docs.gradle.org/current/userguide/kotlin_dsl.html) - For writing Gradle build scripts using Kotlin.

## 🏛️ Architecture

Foodie is based on [***MVVM (Model View View-Model)***](https://developer.android.com/jetpack/docs/guide#recommended-app-arch) architecture and repository pattern.

![architecture](https://developer.android.com/topic/libraries/architecture/images/final-architecture.png)

## 📦 Package Structure
    
    🌐com.example.foodie   # Root Package
    .
    ├──🔌adapters             # Contains adapter classes for RecyclerView, etc
    |
    ├──🧬bindingadapters      # Houses binding adapters for view binding
    |
    ├── 📁data                # Focused on data handling, manipulation, and retrieval
    │   ├── 💾database        # Database-related classes
    │   ├── 📚local           # Pertains to local persistence database, primarily using Room (SQLite)
    |   │   ├── 🔑dao         # Data Access Object(s) for Room to interact with the database 
    │   ├── ☁️remote          # Manages remote data operations     
    |   │   ├── 📡api         # Retrofit API services for remote endpoint communications
    │   └── 📚repository      # Acts as a single source of truth for all data - both local and remote
    |
    ├── 💉di                  # Dependency Injection (DI) related classes           
    │   └── 🧩module          # DI Modules for managing dependencies
    |
    ├── 👤model               # Defines the data models used within the app
    |
    ├── 🖼️ui                  # User Interface layer containing views and their related logic
    │   ├── 📱activities      # Contains Activity classes and their corresponding ViewModel(s)
    │   └── 🖼️fragments       # Houses Fragment classes along with their ViewModel(s)
    |
    ├── 🔧utils               # Utility classes and Kotlin extensions to provide common functionalities across the application
    |
    └── 🧠viewmodels          # ViewModel classes responsible for preparing and managing the data for UI components and handling the communication with the data part of the application

## ⬇️ Installation

To get started with Foodie, follow these steps:

1. Clone the repository:
2. Open the project in your favorite Kotlin-supported IDE.
3. Run the application on your device or emulator.
4. Replace OpenAI Api Key under com.example.foodie/util/Constants/OPENAI_KEY 
5. Replace Spoonacular Api Key under com.example.foodie/util/Constants/FOOD_API_KEY
6. Replace google-services.json under app level folder for Firebase Cloud Messaging

## 🙋 Contributing

Loved the app and want to contribute? Great! We welcome contributions. Send a PR now!

## 📝 License

Foodie is released under the MIT License. See [LICENSE](LICENSE) for details.

---

Happy Cooking! 🎉
