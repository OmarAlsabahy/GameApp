# GameApp - RAWG API Browser

A modern Android application for browsing and searching video games by genre, powered by the [RAWG API](https://rawg.io/apidocs). Built with Clean Architecture, MVVM, and Jetpack Compose.

## 📱 Screenshots

| Home Screen | Loading State | Search Results |
| :---: | :---: | :---: |
| ![Home Screen](screenshots/home_screen.jpg) | ![Loading State](screenshots/loading_state.jpg) | ![Search Results](screenshots/search_results.jpg) |

| Game Details | Promotional |
| :---: | :---: |
| ![Game Details](screenshots/game_details.jpg) | ![Promotional Screen](screenshots/promotional.jpg) |

> [!NOTE]
> Please ensure you place the provided screenshot images in a folder named `screenshots/` at the root of the project with the names used above (`home_screen.jpg`, `loading_state.jpg`, etc.) for them to appear in this README.

## 🛠 Tech Stack

- **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose) with Material 3.
- **Architecture**: MVVM with [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html) (Data, Domain, Presentation layers).
- **Dependency Injection**: [Hilt](https://dagger.dev/hilt/) for robust and testable DI.
- **Networking**: [Retrofit 2](https://square.github.io/retrofit/) with [OkHttp](https://square.github.io/okhttp/).
- **Database**: [Room](https://developer.android.com/training/data-storage/room) for local persistence and offline caching.
- **Async**: Kotlin Coroutines & [Flow](https://kotlinlang.org/docs/flow.html).
- **Image Loading**: [Glide (Compose Support)](https://github.com/bumptech/glide).
- **Styling**: [SDP/SSP Compose](https://github.com/SaravananEzhil/SDP-SSP-Compose) for relative scaling.

## 🏗 Architecture Rationale

The project follows **Clean Architecture** principles to achieve:
1. **Separation of Concerns**: Each layer (Data, Domain, Presentation) has a single responsibility.
2. **Testability**: Business logic is isolated in the Domain layer, making it easy to unit test.
3. **Maintainability**: Changes in the UI (Compose) or Data (Retrofit/Room) don't affect the core business logic.
4. **Scalability**: New features can be added with minimal impact on existing code.

### Layers:
- **Presentation**: UI components (Jetpack Compose) and ViewModels.
- **Domain**: Pure Kotlin business logic, including Repositories interfaces and Use Cases (represented by IRepo definitions).
- **Data**: Retrofit API implementations, Room Database, and Repository implementations.

## ✨ Key Features

- **Genre-Based Browsing**: Explore games categorized by labels like Action, Indie, Adventure, etc.
- **Smart Pagination**: Automatically loads the next page of results as you scroll.
- **Local Search**: Filters already-loaded games in-memory for instant feedback, utilizing Room database queries for high efficiency.
- **Offline-First Support**: Automatically caches trending games, newest games, and genres. The app gracefully falls back to cached data when the internet is unavailable.
- **State Handling**: Comprehensive handling for Loading, Error (with retry), and Empty states.

## 🚀 Getting Started

### 1. API Key
Obtain an API key from [RAWG.io](https://rawg.io/apidocs) and include it in your project's configuration (usually in a network constants file or local.properties).

### 2. Build Instructions
To build and run the app:
```bash
./gradlew assembleDebug
```
You can then install the APK on your device or emulator.

## 📝 Assumptions & Shortcuts
- **Fixed Genres**: The initial genre list is fetched from the API and cached for performance.
- **Image Caching**: Handled automatically by Glide for an optimized scrolling experience.
- **Offline Data**: Only the first few pages of games are cached locally to save device storage.
