## Description
This PR initializes the BorrowHub Backend API using Laravel 11. It sets up the core architecture, integrates authentication, and prepares the environment for further feature development. Key changes include:
- Initialized Laravel project structure in the `backend-api/` directory.
- Integrated **Laravel Sanctum** for secure API token-based authentication.
- Configured the application to prioritize JSON responses for API routes.
- Added basic API routes, including a root health check and a protected user profile endpoint.
- Established the initial database schema with users, password resets, and personal access tokens migrations.
- Set up the testing environment with basic feature and unit tests.
- Updated the backend README with setup and installation instructions.

## Component(s) Affected
- [ ] mobile-app (Android)
- [x] backend-api (Laravel)
- [x] Shared/API Interface
- [x] Documentation/Other

## Related Issue
Closes # (N/A)

## Type of Change
- [ ] Bug fix (non-breaking change which fixes an issue)
- [x] New feature (non-breaking change which adds functionality)
- [ ] Refactor or Architecture Update
- [ ] Documentation Update
- [ ] Breaking change (fix or feature that would cause existing functionality to not work as expected)

## Checklist
- [x] My code follows the code style and architectural guidelines of this project (MVVM for Mobile, MVC/API for Backend).
- [x] I have performed a self-review of my own code.
- [x] I have removed unused imports, variables, and commented-out code.
- [x] I have commented my code, particularly in hard-to-understand areas.
- [x] I have made following changes to the documentation (if applicable).
- [x] My changes generate no new warnings or errors.
- [x] The app (Mobile/Backend) builds and runs successfully on my local machine.

## Screenshots (if applicable)
| Before | After |
| ------ | ----- |
| N/A    | API JSON Response initialized |
