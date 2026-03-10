# BorrowHub Monorepo
### Borrowing Management System — Computer Studies Department

Welcome to the **BorrowHub** project! This is a monorepo that contains the mobile application (Android), the backend API service (Laravel), and a functional web prototype.

---

## Project Structure

- **[mobile-app/](./mobile-app)**: Android Mobile Application (Java, MVVM + Repository Pattern).
- **[backend-api/](./backend-api)**: Laravel Backend API (MVC + Service-Repository Pattern, MySQL, Laravel Sanctum).
- **[borrowhub-prototype/](./borrowhub-prototype)**: Web-based Mobile App Prototype (React, Vite, Tailwind CSS).
- **[docs/](./docs)**: Shared project documentation and contribution guidelines.

---

## Getting Started

Follow these steps to set up the project on your local machine.

### 1. Prerequisites
- **Git** installed on your system.
- **Node.js** (for the prototype).
- **PHP 8.2+** and **Composer** (for the backend).
- **Android Studio** (for the mobile app).
- **MySQL 8.0+** (for the database).

### 2. Fork & Clone
1. **Fork the repository** to your own GitHub account.
2. **Clone your fork locally**:
   ```bash
   git clone https://github.com/your-username/BorrowHub.git
   cd BorrowHub
   ```

---

## Development Setup

### For Mobile Developers
The Android application is located in the `mobile-app/` directory.
- **Architecture:** MVVM + Repository.
- **Language:** Java.
- **Setup:** Open the `mobile-app/` folder in Android Studio and sync Gradle.
- **Full Guide:** See **[mobile-app/README.md](./mobile-app/README.md)**.

### For Backend Developers
The Laravel API is located in the `backend-api/` directory.
- **Architecture:** MVC + Service-Repository Pattern.
- **Database:** MySQL.
- **Auth:** Laravel Sanctum.
- **Setup:** Navigate to `backend-api/`, install dependencies, and configure your `.env` file.
- **Full Guide:** See **[backend-api/README.md](./backend-api/README.md)**.

### For Prototype Viewing
The web-based prototype is located in the `borrowhub-prototype/` directory.
- **Setup:**
  ```bash
  cd borrowhub-prototype
  npm install
  npm run dev
  ```
- **Full Guide:** See **[borrowhub-prototype/README.md](./borrowhub-prototype/README.md)**.

---

## Shared Resources

- **[CONTRIBUTING.md](./docs/CONTRIBUTING.md)**: Guidelines for contributing.
- **[.github/](./.github)**: Standard Issue and Pull Request templates.

## Architecture Overview

BorrowHub uses a **Network-First (with Local Cache)** architecture. The mobile app communicates with the Laravel API to sync data with a central MySQL database, while using Room for offline support.

- **Mobile Architecture:** [MVVM + Repository Guide](./mobile-app/docs/ARCHITECTURE.md)
- **Backend Architecture:** [Service-Repository Guide](./backend-api/docs/ARCHITECTURE.md)

---
*BorrowHub — Making asset management simple and efficient.*
