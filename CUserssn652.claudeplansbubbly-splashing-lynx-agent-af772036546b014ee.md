# UI/UX Redesign Plan: Student Grade Tracker

## 1. Proposed Class Structure

To transform the current basic GUI into a professional dashboard, we will introduce custom UI components to replace standard Swing elements and organize the panels for better maintainability.

### Custom Component Classes
- **`RoundedPanel` (extends `JPanel`)**: 
    - Overrides `paintComponent` to render rounded corners using `Graphics2D`.
    - Supports configurable corner radius and border colors.
- **`ModernButton` (extends `JButton`)**: 
    - Implements custom hover and pressed states (color transitions).
    - Removes default borders and focus rings.
    - Adds internal padding and center-aligned text.
- **`ModernTable` (Helper/Utility)**: 
    - Provides methods to apply consistent styling to `JTable` (row height, alternating colors, header fonts).

### Layout Architecture
- **`StudentGradeTrackerGUI` (Main Frame)**: 
    - Maintains the `GradeManager` instance.
    - Manages the `CardLayout` for page transitions.
    - Implements the primary sidebar and content area shells.
- **Page-specific Panel Logic** (implemented as private methods or inner classes):
    - `createDashboardPanel()`
    - `createStudentsPanel()`
    - `createAddStudentPanel()`
    - `createAddGradePanel()`
    - `createDetailsPanel()`
    - `createSummaryPanel()`

---

## 2. Detailed Styling Specifications

### Color Palette
| Element | Hex Code | Role |
|---|---|---|
| **Sidebar BG** | `#212529` | Dark Charcoal/Navy - provides professional contrast |
| **Main BG** | `#F8F9FA` | Off-white - reduces eye strain in content areas |
| **Accent** | `#0D6EFD` | Primary Blue - used for active buttons and highlights |
| **Card BG** | `#FFFFFF` | Pure White - makes content "pop" from the background |
| **Border** | `#DEE2E6` | Light Gray - subtle separation of elements |
| **Text Primary** | `#212529` | High contrast for main headings |
| **Text Muted** | `#6C757D` | Lower contrast for subtitles and labels |

### Typography & Spacing
- **Font**: "Segoe UI" (System default for Windows, clean and modern).
- **Hierarchy**:
    - Header Titles: **Bold**, 24-28px.
    - Subtitles: Plain, 16px, Muted color.
    - Table/Form Text: Plain, 14px.
- **Row Height**: `JTable` rows set to 35-40px for better readability.
- **Padding**: Generous `EmptyBorder` (20-40px) around main content areas to prevent cramping.

---

## 3. Layout Strategy

### Main Application Shell
- **Outer Container**: `BorderLayout`.
    - **West**: Sidebar (Fixed width 260px, `BoxLayout` Y-Axis).
    - **Center**: `JPanel` with `CardLayout` (The "Page" container).

### Individual Page Layouts
1. **Dashboard**:
    - **North**: Header section (Greeting: "Good evening", Subtitle: "Academic Overview").
    - **Center**: `GridLayout` (2x3) containing `RoundedPanel` stat cards. Each card uses `BorderLayout` (North: Label, Center: Large Value).
2. **Students List**:
    - **North**: Header + Search bar (Right-aligned `FlowLayout`).
    - **Center**: `JScrollPane` wrapping a `ModernTable`.
    - **South**: `FlowLayout` (Right) for "Details" and "Remove" buttons.
3. **Add Student/Grade Forms**:
    - **Center**: A single `RoundedPanel` centered using `GridBagLayout`.
    - **Inside Panel**: `GridBagLayout` for labeled form fields (ID, Name, Grade).
4. **Student Details**:
    - **North**: Profile Card (`RoundedPanel`) showing ID and Name in a bold header style.
    - **Center**: `ModernTable` showing a history of all grades recorded for that student.
5. **Summary Report**:
    - **North**: Page title.
    - **Center**: `ModernTable` displaying global statistics (Avg, High, Low per student).

---

## 4. Data Integrity & Validation Plan

To ensure a robust user experience, we will implement a multi-layer validation strategy.

### Input Validation
- **Null/Empty Checks**: Ensure ID and Name fields are not blank before submission.
- **Type Safety**: Wrap `Double.parseDouble()` in try-catch blocks to handle non-numeric grade inputs.
- **Range Checks**: Validate that grades are within $[0, 100]$ before calling `GradeManager`.
- **Uniqueness**: Leverage `GradeManager.addStudent`'s exception handling to notify the user if an ID is already taken.

### UI Feedback Loop
- **Success Notifications**: Use `JOptionPane.showMessageDialog` with `INFORMATION_MESSAGE` for successful adds/saves.
- **Error Alerts**: Use `ERROR_MESSAGE` for validation failures with clear, human-readable instructions.
- **Dynamic State**: Trigger `updateDynamicContent()` after every modification to ensure the Dashboard and Tables are always in sync with the `GradeManager` state.

### Persistence
- Ensure `FileManager.saveData` and `loadData` are called via the sidebar actions, with appropriate error handling to prevent app crashes on corrupted files.
