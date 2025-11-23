# Project: ComposeBridge

[한국어 버전 보기 (Korean Version)](README_ko.md)

## 🚀 Project Overview

This project aims to break down UI/UX communication barriers between developers and clients while revolutionizing the Android UI development process. Through natural language-based UI generation, real-time UI rendering via JSON from Figma designs, and automated generation of optimized Compose UI code using the Gemini API, we provide an environment where clients can directly manipulate UI elements and receive immediate feedback. Ultimately, this dramatically reduces unnecessary communication between developers and clients, maximizing productivity.

## ✨ Key Features

*   **Natural Language-Based Android UI Generation**: Generate Android UI components through natural language commands from users.
*   **Serverless JSON-Based UI Rendering**: Utilize lightweight JSON files to instantly render UI within Android apps without requiring a server.
*   **Figma Integration and Code Conversion**: Extract design information from Figma API into JSON format, then automatically convert it into state-optimized Compose UI code via Gemini API.
*   **Real-time UI Feedback System**: Enable clients to directly adjust colors, button sizes, positions, and other elements within the app while viewing changes in real-time and providing feedback.
*   **Minimized Developer-Client Communication**: Automate design changes and requirement implementation processes to significantly reduce communication costs for both parties.

## 🏗️ Architecture & Tech Stack (Android)

The Android application for this project is built on the following architecture and technology stack:

*   **Architecture**:
    *   **MVVM (Model-View-ViewModel)**: Separate UI and business logic to enhance maintainability and testability.
    *   **Clean Architecture**: Introduce a `Domain Layer` to ensure business logic independence and minimize dependencies between layers.
*   **UI Framework**:
    *   **Jetpack Compose**: Use declarative UI framework for fast and efficient UI development.
    *   **Single Activity Architecture**: Leverage Compose UI advantages by structuring the entire app with a single `Activity`.
*   **Modularization**:
    *   **Multi-Module**: Separate modules by functionality and layer to enhance code reusability and reduce build time.
*   **Dependency Injection (DI)**:
    *   **Hilt**: Use Dagger Hilt to automate dependency injection and reduce code coupling.
*   **Core Technologies**:
    *   **JSON File-Based Instant Compose UI Rendering**: Parse JSON data extracted from Figma to dynamically generate Compose UI and render it immediately on screen.
    *   **Figma API Integration**: Directly read Figma design files to extract UI component information.
    *   **Gemini API Utilization**: Since initial code provided by Figma API may be inefficient, use Gemini API to convert it into `state-optimized Compose UI code` and improve it.

## ⚠️ Exception Handling & Feedback Loop

*   **Feedback on Code Conversion Failures**: When Gemini API fails to modify certain hardcoded parts from Figma, provide a feedback mechanism that clearly informs users about these parts and allows them to request additional modifications. This promotes continuous code quality improvement.

## 🛠️ Getting Started

Setup guide for running the project in your local environment:

1.  **Clone**:
    ```bash
    git clone [https://github.com/largeblueberry/ComposeBridge.git]
    cd [PROJECT_FOLDER_NAME]
    ```
2.  **Android Studio**: Android Studio Bumblebee (2021.1.1) or higher is recommended.
3.  **SDK Version**: Set to `minSdk` 35, `targetSdk` 35.
4.  **Build**: Open the project in Android Studio and run `Build > Make Project`.

## 🤝 Contributing

If you'd like to contribute to this project, please refer to the following guidelines:

1.  Create issues to make suggestions or report bugs.
2.  Submit code changes through Pull Requests.

## 📄 License

This project is distributed under the **Apache License 2.0**. Please refer to the `LICENSE` file for details.