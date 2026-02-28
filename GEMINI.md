# BorrowHub - Project Context & Guidelines

## Project Overview
**BorrowHub** is an Android-based Borrowing Management System designed for University MIS and CSD Staff. It manages equipment, devices, and asset borrowing transactions within the Computer Studies Department.

- **Platform:** Android (Java)
- **Minimum SDK:** 24 (Android 7.0)
- **Target/Compile SDK:** 36
- **Language:** Java 11
- **Architecture:** MVVM (Model-View-ViewModel) with Repository Pattern
- **Database:** Room (SQLite)
- **UI Framework:** XML Layouts with View Binding

## Architecture Details
BorrowHub follows a streamlined MVVM approach where the **Entity Layer acts as the Domain Model** across all layers (Data -> Repository -> ViewModel -> View) to minimize boilerplate code.

### Layer Responsibilities
1.  **UI Layer (view/):** Activities and Fragments. Responsible for rendering UI and sending user events to the ViewModel. Uses View Binding for layout interaction.
2.  **ViewModel Layer (viewmodel/):** Holds UI state using `LiveData`. Processes business logic and calls Repository methods. Does not have references to Android Context or DAOs.
3.  **Repository Layer (repository/):** The single source of truth for data. Coordinates between the local database (Room) and any future remote sources. Manages threading for database operations.
4.  **Data Layer (data/local/):**
    -   **Entities:** Java classes annotated with `@Entity` that map to SQLite tables.
    -   **DAOs:** Interfaces annotated with `@Dao` defining SQL operations.
    -   **AppDatabase:** RoomDatabase singleton class.

## Building and Running
The project uses Gradle (Kotlin Script) for build management.

- **Clean Project:** `./gradlew clean`
- **Build Debug APK:** `./gradlew assembleDebug`
- **Run Unit Tests:** `./gradlew test`
- **Run Instrumentation Tests:** `./gradlew connectedAndroidTest`
- **Lint Check:** `./gradlew lint`

## Development Conventions

### Coding Style
- **Naming:** Follow standard Java/Android naming conventions (PascalCase for classes, camelCase for methods/variables).
- **View Binding:** Always use View Binding in Activities and Fragments instead of `findViewById`. Initialize binding in `onCreate` (Activities) or `onCreateView` (Fragments).
- **Threading:** Never perform database operations on the Main/UI thread. Use the `ExecutorService` defined in the Repository to run operations in the background.

### Data Flow
- **Reactive UI:** Use `LiveData` for data that needs to be observed by the UI. Room DAOs should return `LiveData<List<Entity>>` or `LiveData<Entity>` for automatic updates.
- **Repository Pattern:** ViewModels must only interact with Repositories, never directly with DAOs or the Database.
- **Single Model:** Use Room Entities directly as models in the View/ViewModel layers as per project convention (see `docs/ARCHITECTURE.md`).

### Repository Structure
- `app/src/main/java/com/example/borrowhub/`
    - `data/local/`: Room configuration, DAOs, and Entities.
    - `repository/`: Repository classes.
    - `view/`: Activities and Fragments.
    - `viewmodel/`: ViewModel classes.
    - `adapter/`: RecyclerView Adapters.
    - `utils/`: Helper and utility classes.

## Documentation
- [README.md](README.md) - General overview and setup instructions.
- [ARCHITECTURE.md](docs/ARCHITECTURE.md) - Detailed breakdown of the MVVM implementation.
- [CONTRIBUTING.md](docs/CONTRIBUTING.md) - Workflow and contribution guidelines.
