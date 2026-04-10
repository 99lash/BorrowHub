# BorrowHub Design System

This document outlines the visual language, UI components, and design tokens used in the **BorrowHub** mobile application. It ensures consistency across the interface and serves as a reference for future implementations.

---

## 🎨 Color Palette

BorrowHub uses a dual-themed color system (Light and Dark) based on a primary Crimson brand color and a refined gray scale.

### 🔴 Brand Colors
| Token | Light Value | Dark Value | Usage |
| :--- | :--- | :--- | :--- |
| `primary_crimson` | `#C94C5D` | `#D06C7A` | Main actions, highlights, branding |
| `primary_crimson_dark` | `#A43E4F` | `#B85A68` | Status bar, primary button states |
| `primary_crimson_darker` | `#8B3643` | `#9C4A58` | Deep accents |

### ⚪ Grayscale (Neutral)
| Token | Light Value | Dark Value | Usage |
| :--- | :--- | :--- | :--- |
| `gray_50` | `#F7F8FB` | `#1D232E` | Page backgrounds (Light), Card Surface (Dark) |
| `gray_100` | `#EEF1F5` | `#252C37` | Card backgrounds (Light), Secondary surfaces |
| `gray_200` | `#E1E5EA` | `#2E3541` | Dividers, Borders (Light) |
| `gray_500` | `#7B8492` | `#949CB0` | Subtitles, disabled text |
| `gray_700` | `#4A5565` | `#C0C8D6` | Body text, icons |
| `gray_900` | `#1F2933` | `#131922` | Primary titles, dark mode background |

### 🟢 Status & Semantic
| State | Text Color | Background Color | Description |
| :--- | :--- | :--- | :--- |
| **Available** | `#2F7D5A` | `#E6F3EC` | Items ready for borrowing |
| **Borrowed** | `#A26A2A` | `#F7EEDC` | Items currently out |
| **Maintenance** | `#B25555` | `#F5E4E4` | Broken or restricted items |
| **Error** | `#DC2626` | — | Destructive actions, validation errors |

---

## 📐 Shapes & Elevation

The application follows a "Soft UI" approach with generous corner radii to improve accessibility and aesthetics.

| Token | Value | Applied To |
| :--- | :--- | :--- |
| **Extra Large** | `24dp` | Dialogs, Bottom Sheets, Profile Dropdown |
| **Large** | `16dp` | Primary List Cards (Items, Users, Students) |
| **Medium** | `12dp` | Input Fields, Success Cards, Detail Cards |
| **Small/Badge** | `8dp` | Status Tags, Course Badges |

---

## 🔤 Typography

BorrowHub uses the system default font (Roboto/Inter) with a focus on hierarchy through weight and size.

| Level | Size | Weight | Usage |
| :--- | :--- | :--- | :--- |
| **Display** | `28sp` | Bold | Dashboard / Main Screen Titles |
| **Headline** | `22sp` | Bold | Fragment Page Titles |
| **Title** | `15sp` | Bold | Card Headers, Dialog Titles |
| **Body** | `14sp` | Regular | Primary Information |
| **Caption** | `12sp` | Semi-bold | Subtitles, Timestamps, Badges |

---

## 🧱 Key UI Components

### 1. The Inventory Card (`item_inventory_row.xml`)
- **Background:** `gray_100`
- **Corners:** `16dp`
- **Layout:** Horizontal orientation with Title, Type, Status Badge, and Stock count.

### 2. Status Badges (`bg_status_badge.xml`)
- **Corners:** `8dp`
- **Padding:** `8dp` horizontal, `4dp` vertical.
- **Coloring:** Dynamic based on the item status (Available/Borrowed).

### 3. Input Fields (`bg_input.xml`)
- **Background:** `gray_50`
- **Stroke:** `1dp` of `gray_200`
- **Corners:** `12dp`
- **Interactions:** Uses Material 3 `TextInputLayout` for animations and error handling.

### 4. Bottom Navigation
- **Active Tint:** `primary_crimson`
- **Inactive Tint:** `gray_500`
- **Indicator:** Hidden background container (Transparent) to maintain a clean look.

---

## 🖼 Iconography

Icons are consistent **24dp x 24dp** vector drawables.
- **Style:** Mostly outlined with `1.5` to `2.0` stroke width.
- **Common Icons:** `ic_home`, `ic_inventory`, `ic_transaction`, `ic_logs`, `ic_profile_toolbar`.
- **Action Icons:** `ic_edit` (Blue/Primary), `ic_delete` (Red/Error).
