# Implementation Plan: Theme Toggler (Light/Dark Mode)

## Objective
Implement a fully functional Light/Dark Mode toggler inside the `menu_profile.xml` of the BorrowHub Android application. The icon and text should scale correctly relative to the standard Material design menu guidelines, and the user's preference should be saved across app sessions.

## Key Files & Context
- **Menu Layout**: `mobile-app/app/src/main/res/menu/menu_profile.xml`
- **Logic**: `mobile-app/app/src/main/java/com/example/borrowhub/view/MainActivity.java`
- **Data Persistence**: `mobile-app/app/src/main/java/com/example/borrowhub/data/local/SessionManager.java`
- **Drawables**: New vector icons `ic_theme_dark.xml` (Moon) and `ic_theme_light.xml` (Sun)

## Implementation Steps

### 1. Vector Drawables Creation
Create two Material Design vector drawables (`ic_theme_dark.xml` and `ic_theme_light.xml`) inside `mobile-app/app/src/main/res/drawable/`.
- **Sizing Note**: We will use the standard `24dp` viewport which natively scales relative to the menu font size and action bar dimensions, ensuring it is perfectly proportioned.

### 2. Update `menu_profile.xml`
Add a new menu item for the theme toggle.
```xml
<item
    android:id="@+id/action_toggle_theme"
    android:title="Toggle Theme"
    android:icon="@drawable/ic_theme_dark"
    app:showAsAction="always" />
```

### 3. Update `SessionManager.java`
Add preference management to persist the user's theme choice so the app remembers it upon restarting.
- Add `KEY_DARK_MODE` constant.
- Add `setDarkMode(boolean isDark)` and `isDarkMode()` methods.

### 4. Update `MainActivity.java`
- **On App Start**: In `onCreate`, retrieve the saved theme state from `SessionManager` and apply it using `AppCompatDelegate.setDefaultNightMode()`.
- **Menu Logic**: Inside `binding.topAppBar.setOnMenuItemClickListener`, add a case for `R.id.action_toggle_theme`.
  - Toggle the current state.
  - Save the new state in `SessionManager`.
  - Apply the new mode immediately using `AppCompatDelegate.setDefaultNightMode()`.
- **Dynamic Icon Update**: Retrieve the `MenuItem` from `binding.topAppBar.getMenu()` and update its icon to match the current mode.

## Verification & Testing
- Tapping the toggler successfully switches between `MODE_NIGHT_YES` and `MODE_NIGHT_NO`.
- The app restarts the UI dynamically applying the new color scheme without crashing.
- Closing and reopening the app retains the chosen theme (persisted via SharedPreferences).
- The toggle icon scales correctly and does not appear pixelated or misaligned with the text.