# Project Name: ComposeMarket

[한국어 버전 (Korean Version)](README_ko.md)

## 🚀 Project Overview

**"Don't build UI. Shop for it."**

**ComposeMarket** is an **'Android UI Marketplace & Rendering Engine'** designed to revolutionize the repetitive UI modification process between developers and non-technical stakeholders (e.g., planners, designers, clients).

We eliminate the inefficient "Modify → Build → Check" loop. Instead, we offer a solution where you can **select (Shop)** a UI style tailored to a specific target persona and **instantly render** it in the app, ready for export.

## 💡 Problem & Solution

*   **The Problem**: Stakeholders often give abstract feedback like "Make it softer" or "Design it for Gen Z." Developers have to modify code and rebuild the app every time to visualize these abstract changes.
*   **The Solution**: With ComposeMarket, you simply select a defined persona (e.g., **'Teenager', 'Office Worker', 'Silver Generation'**). The app **instantly renders** the UI optimized for that target **without restarting**. If you like the result, you can export the screen and the style data (JSON) directly.

## ✨ Key Features

*   **Persona-Based UI Selector (UI Shopping)**:
    *   Select a target audience (10s, 20s, Business, Silver, etc.), and the app applies a verified UI style optimized for that target immediately.
*   **Real-time Dynamic Rendering Engine**:
    *   This is not simple image swapping. Utilizing **Jetpack Compose UI**, the engine injects UI attributes (`Color`, `Shape`, `Typography`) at runtime, allowing native components to redraw themselves dynamically in real-time. (Currently, only color changes are fully implemented.)
*   **One-Click Export**:
    *   When you find a style you like, press the 'Stamp' button. The current UI configuration is immediately copied to the clipboard as a lightweight JSON Style Map.
*   **Serverless & Offline First**:
    *   The system operates entirely via a lightweight internal JSON/Style Map engine, ensuring zero latency and an immediate, server-communication-free user experience.

## 🏗️ Architecture & Tech Stack (Android)

This project is built with a robust architecture designed for scalability and maintainability.

*   **Architecture**:
    *   **MVVM + Architecture**: Strict separation of UI logic and business logic.
    *   **Dynamic Theming Engine**: A **Data-Driven UI** design that flexibly changes via `UiStyleConfig` object injection, rather than hardcoded UI.
*   **UI Framework**:
    *   **Jetpack Compose**: Built 100% with Kotlin's declarative UI, leveraging immediate Recomposition based on State changes.
*   **Core Technologies**:
    *   **Runtime UI Replacement**: Applied a design pattern to swap the entire app's UI theme strategy at runtime when the target persona changes.
    *   **Git Abstraction**: Implemented a "Time Machine" UI/UX to allow non-developers to intuitively browse and explore UI changes.

## 📱 Persona-Based UI Style Preview

The core of ComposeMarket is demonstrating how UI styles change in real-time based on the target audience. Below are examples of how the same screens are rendered differently for key personas like the **'Teenager'**, **'20s Woman'**, and **'30s Office Worker'**.

| Persona (Target) | Screen Type | Applied Style Preview |
|:---:|:---:|:---:|
| **Teenager (10s)** | Login Screen | <img src="images/loginScreen_students_10s.jpg" width="200" alt="Teenager Login Screen"> |
| **Woman (20s)** | Voice Recording Screen | <img src="images/recordScreen_woman_20s.jpg" width="200" alt="20s Woman Voice Recording Screen"> |
| **Office Worker (30s)** | Profile Settings (Approved) | <img src="images/profileScreen_office_30s_approve.jpg" width="200" alt="30s Office Worker Profile Screen (Approved)"> |
| **Office Worker (30s)** | Profile Settings (Default) | <img src="images/profileScreen_office_30s.jpg" width="200" alt="30s Office Worker Profile Screen (Default)"> |

### 📤 Final Result Export (Delivery Feature)

After selecting a preferred style, non-developers can easily communicate the result to the development team through the following export features:

1.  **JSON Style Map Export**: Copies the lightweight JSON file containing all applied design tokens (colors, typography, spacing, etc.) for the current screen to the clipboard.
2.  **Time Machine PDF Export**: Generates a PDF report combining a bitmap capture of the selected UI screen with the applied JSON style map. (Used for final approval by planners/designers.)

## 🛠️ Getting Started

1.  **Clone**:
    ```bash
    git clone https://github.com/largeblueberry/ComposeBridge.git
    ```
2.  **Requirements**:
    *   Android Studio Ladybug (2024.2.1) or higher recommended
    *   JDK 17 or higher
    *   minSdk 35 / targetSdk 35
3.  **Run**:
    *   Open the project and run the `app` module to experience 'ComposeMarket' immediately.

## 🔮 Future Roadmap

*   **JSON Improvement**: Expanding beyond color changes to allow for component-level modification via JSON.
*   **Git Abstraction Enhancement**: Improving the "Time Machine" UI with actual animation development for a more intuitive user experience.
*   **UI Marketplace Expansion**: Planning to offer a wider variety of screens and components.
*   **Platform Expansion**: Considering expansion to other platforms like JavaScript and iOS.
*   **Team Functionality**: Planning features to allow teams to manage and share UI styles collaboratively.

## 📄 License

This project is distributed under the **Apache License 2.0**. Please refer to the `LICENSE` file for details.