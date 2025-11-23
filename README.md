# Project Name: ComposeMarket (Project Pegasus)

[한국어 버전 (Korean Version)](README_ko.md)

## 🚀 Project Overview

**"Don't build UI. Shop for it."**

**ComposeMarket** is an **'Android UI Marketplace & Rendering Engine'** designed to revolutionize the repetitive UI modification process between developers and clients.

We eliminate the inefficient "Modify -> Build -> Check" loop. Instead, we offer a solution where you can **select (Shop)** a UI style tailored to a specific target persona and **instantly convert (Delivery)** it into production-ready code. This is achieved through our core technology, **"Dynamic UI Injection"**.

## 💡 Problem & Solution

*   **The Problem**: Clients often give abstract feedback like "Make it softer" or "Design it for Gen Z." Developers have to modify code and rebuild the app every time to show the changes.
*   **The Solution**: With ComposeMarket, you simply select a defined persona (e.g., 'Teenager', 'Office Worker', 'Silver Generation'). The app **instantly transforms** the UI (Fonts, Colors, Layouts, Interactions) without restarting. If you like what you see, you can export the code with a single click.

## ✨ Key Features

*   **Persona-Based UI Selector (UI Shopping)**:
    *   Select a target audience (10s, 20s, Business, Silver, etc.), and the app applies a verified UI style optimized for that target immediately.
*   **Real-time Dynamic Rendering Engine**:
    *   It's not just swapping images. We utilize the **Strategy Pattern** to inject UI attributes (Color, Shape, Typography) at runtime, redrawing native components in real-time.
*   **One-Click Code Export (Code Delivery)**:
    *   Like the UI you see? Press the 'Stamp' button to copy **Clean Compose Code** directly to your clipboard, ready to be pasted into Android Studio.
*   **Serverless & Offline First**:
    *   Powered by a lightweight internal JSON/Style Map engine, ensuring zero latency and an immediate user experience without complex server interactions.

## 🏗️ Architecture & Tech Stack (Android)

This project is built with a robust architecture designed for scalability and maintainability.

*   **Architecture**:
    *   **MVVM + Clean Architecture**: Strict separation of UI logic and business logic.
    *   **Dynamic Theming Engine**: A **Data-Driven UI** design that flexibly changes via `UiStyleConfig` object injection, rather than hardcoded UI.
*   **UI Framework**:
    *   **Jetpack Compose**: Built 100% with Kotlin's declarative UI, leveraging immediate Recomposition based on State changes.
*   **Core Technologies**:
    *   **Strategy Pattern Implementation**: Applied a design pattern that swaps the entire app's theme strategy at runtime when the target persona changes.
    *   **Custom Code Generator**: Includes logic to reverse-engineer selected style attributes (Hex Colors, Dp, Shapes) into developer-friendly Kotlin code.

## 📱 Preview

*(Insert screenshots or GIFs of the app running here)*

| Target Selection (Shopping) | Real-time Rendering (Preview) | Code Export (Delivery) |
|:---:|:---:|:---:|
| <Target Scroll Screen> | <Style Changed Screen> | <Code Copied Toast> |

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

*   **AI Style Generator**: Moving beyond presets to integrate Gemini API for natural language prompts like "Give me a warm winter vibe UI" (Planned for v2.0).
*   **Figma Plugin Integration**: Building a pipeline to extract designer work into JSON and transmit it directly to the app.

## 🤝 Contributing

Want to add more UI theme presets? Define a `UiStyleConfig` object and send us a PR.

## 📄 License

This project is distributed under the **Apache License 2.0**. Please refer to the `LICENSE` file for details.