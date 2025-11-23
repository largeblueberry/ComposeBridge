# 프로젝트명: ComposeMarket (Project Pegasus)

[English Version](README.md)

## 🚀 프로젝트 개요

**"UI를 개발하지 마세요. 쇼핑하세요."**

**ComposeMarket**은 개발자와 클라이언트 간의 소모적인 UI 수정 과정을 혁신하기 위해 탄생한 **'안드로이드 UI 마켓플레이스 & 렌더링 엔진'**입니다.

기존의 "수정 -> 빌드 -> 확인"이라는 비효율적인 루프를 제거하고, **타겟 사용자(Persona)에 맞는 UI 스타일을 실시간으로 선택(Shopping)하고, 즉시 프로덕션 레벨의 코드로 변환(Delivery)**해주는 솔루션입니다. **"Dynamic UI Injection"** 기술을 통해 구현했습니다.

## 💡 핵심 문제 해결 (Problem & Solution)

*   **Problem**: 기획자/클라이언트는 "조금 더 부드러운 느낌으로", "MZ세대 타겟으로" 같은 추상적인 요구를 합니다. 개발자는 이를 위해 매번 코드를 수정하고 다시 빌드해야 합니다.
*   **Solution**: ComposeBridge는 **'10대 학생', '30대 직장인', '실버 세대'** 등 정의된 페르소나를 선택하기만 하면, **앱이 재실행 없이 즉시 해당 타겟에 최적화된 UI(폰트, 컬러, 레이아웃, 인터랙션)로 변신**합니다. 마음에 들면 버튼 하나로 해당 코드를 가져갈 수 있습니다.

## ✨ 주요 기능 (Key Features)

*   **Persona-Based UI Selector (UI 쇼핑)**:
    *   사용자 타겟층(10대, 20대, 직장인, 실버세대 등)을 선택하면, 해당 타겟에 검증된 UI 스타일이 즉시 적용됩니다.
*   **Real-time Dynamic Rendering Engine**:
    *   단순한 이미지 교체가 아닙니다. **Strategy Pattern**을 활용하여 런타임에 UI 속성(Color, Shape, Typography)을 주입, 실제 작동하는 네이티브 컴포넌트를 실시간으로 다시 그립니다.
*   **One-Click Code Export (코드 배달)**:
    *   화면에서 보고 있는 UI가 마음에 드나요? 'Stamp' 버튼을 누르면 즉시 안드로이드 스튜디오에 붙여넣기 가능한 **Clean Compose Code**가 클립보드에 복사됩니다.
*   **Serverless & Offline First**:
    *   복잡한 서버 통신 없이 앱 내부의 경량화된 JSON/Style Map 엔진을 통해 동작하여 지연 시간 없는 즉각적인 경험을 제공합니다.

## 🏗️ 아키텍처 및 기술 스택 (Android)

본 프로젝트는 확장성과 유지보수를 고려하여 견고한 아키텍처로 설계되었습니다.

*   **아키텍처**:
    *   **MVVM + Clean Architecture**: UI 로직과 비즈니스 로직을 철저히 분리.
    *   **Dynamic Theming Engine**: 하드코딩된 UI가 아닌, `UiStyleConfig` 객체 주입을 통해 유연하게 변하는 **Data-Driven UI** 설계.
*   **UI 프레임워크**:
    *   **Jetpack Compose**: 100% Kotlin 기반의 선언형 UI로, 상태(State) 변화에 따른 즉각적인 리컴포지션(Recomposition)을 활용했습니다.
*   **핵심 기술**:
    *   **Strategy Pattern Implementation**: 타겟 페르소나 변경 시, 앱 전체의 테마 전략을 런타임에 교체하는 디자인 패턴 적용.
    *   **Custom Code Generator**: 선택된 스타일 속성(Hex Color, Dp, Shape)을 파싱하여, 개발자가 바로 사용할 수 있는 Kotlin 코드로 역변환(Reverse Engineering)하는 로직 탑재.

## 📱 스크린샷 및 시연 (Preview)

*(여기에 앱 실행 스크린샷이나 GIF를 넣으면 좋습니다)*

| 타겟 선택 (Shopping) | 실시간 렌더링 (Preview) | 코드 생성 (Export) |
|:---:|:---:|:---:|
| <타겟 스크롤 화면> | <스타일 변경된 화면> | <복사된 코드 토스트> |

## 🛠️ 개발 환경 설정 (Getting Started)

1.  **클론**:
    ```bash
    git clone https://github.com/largeblueberry/ComposeBridge.git
    ```
2.  **요구 사항**:
    *   Android Studio Ladybug (2024.2.1) 이상 권장
    *   JDK 17 이상
    *   minSdk 35 / targetSdk 35
3.  **실행**:
    *   프로젝트를 열고 `app` 모듈을 실행하면 즉시 'Pegasus Market'을 경험할 수 있습니다.

## 🔮 향후 로드맵 (Future Roadmap)

*   **AI Style Generator**: 현재의 프리셋 방식을 넘어, Gemini API를 연동하여 "겨울 느낌의 따뜻한 UI 줘"와 같은 자연어 프롬프트 처리 기능 (v2.0 예정).
*   **Figma Plugin 연동**: 디자이너의 작업물을 JSON으로 추출하여 앱으로 바로 전송하는 파이프라인 구축.

## 🤝 기여 (Contributing)

UI 테마 프리셋을 추가하고 싶으신가요? `UiStyleConfig` 객체를 정의하여 PR을 보내주세요.

## 📄 라이선스 (License)

본 프로젝트는 **Apache License 2.0** 하에 배포됩니다.