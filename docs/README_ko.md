# 프로젝트명: ComposeMarket

[English Version](README.md)

## 🚀 프로젝트 개요

**"UI를 쇼핑하세요."**

**ComposeMarket**은 개발자와 비개발자 사이의 소모적인 UI 수정 과정을 혁신하기 위해 만든 **안드로이드 UI 마켓플레이스 & 렌더링 엔진**입니다.

기존의 "수정 -> 빌드 -> 확인"이라는 비효율적인 루프를 제거하고, 타겟 사용자(Persona)에 맞는 UI 스타일을 실시간으로 선택(Shopping)하고, 화면과 json과 함께 내보내는 솔루션입니다.  기술을 통해 구현했습니다.

## 💡 핵심 문제 해결 (Problem & Solution)

*   **Problem**: 기획자/클라이언트는 "조금 더 부드러운 느낌으로", "MZ세대 타겟으로" 같은 추상적인 요구를 합니다. 개발자는 이를 위해 매번 코드를 수정하고 다시 빌드해야 합니다.
*   **Solution**: ComposeBridge는 **'10대 학생', '30대 직장인', '실버 세대'** 등 정의된 페르소나를 선택하기만 하면, **앱이 재실행 없이 즉시 해당 타겟에 최적화된 UI를 랜더링**합니다. 마음에 들면 화면과 JSON 형태로 내보내 개발자가 바로 사용할 수 있습니다.

## ✨ 주요 기능 (Key Features)

*   **Persona-Based UI Selector (UI 쇼핑)**:
    *   사용자 타겟층(10대, 20대, 직장인, 실버세대 등)을 선택하면, 해당 타겟에 검증된 UI 스타일이 즉시 적용됩니다.
*   **Real-time Dynamic Rendering Engine**:
    *   단순한 이미지 교체가 아닌, **Compose UI**를 활용하여 런타임에 UI 속성(Color, Shape, Typography)을 주입, 실제 작동하는 네이티브 컴포넌트를 실시간으로 다시 그립니다. (현재는 색상만 변경이 가능합니다.)
*   **One-Click Export**:
    *   화면에서 보고 있는 UI가 마음에 들면, 'Stamp' 버튼을 누르면 됩니다. 그러면, 즉시 JSON 형태로 클립보드에 복사됩니다.
*   **Serverless & Offline First**:
    *   서버 통신 없이 앱 내부의 경량화된 JSON/Style Map 엔진을 통해 동작하여 지연 시간 없는 즉각적인 경험을 제공합니다.

## 🏗️ 아키텍처 및 기술 스택 (Android)

본 프로젝트는 확장성과 유지보수를 고려하여 견고한 아키텍처로 설계되었습니다.

*   **아키텍처**:
    *   **MVVM + Architecture**: UI 로직과 비즈니스 로직을 철저히 분리.
    *   **Dynamic Theming Engine**: 하드코딩된 UI가 아닌, `UiStyleConfig` 객체 주입을 통해 유연하게 변하는 **Data-Driven UI** 설계.
*   **UI 프레임워크**:
    *   **Jetpack Compose**: 100% Kotlin 기반의 선언형 UI로, 상태(State) 변화에 따른 즉각적인 리컴포지션(Recomposition)을 활용했습니다.
*   **핵심 기술**:
    *   **로컬 ui 변경 구현**: 타겟 페르소나 변경 시, 앱 전체의 ui를 런타임에 교체하는 디자인 패턴 적용.
    *  **Git 추상화**: 타임머신 UI/UX를 도입하여, 비 개발자도 직관적으로 UI 변경 사항을 탐색할 수 있도록 구현.

## 📱 페르소나별 UI 스타일 미리보기 (Persona-Based Preview)

ComposeMarket의 핵심은 타겟 사용자에 따라 UI 스타일이 실시간으로 변화하는 것을 보여주는 것입니다. 아래는 **'10대 학생'**, **'20대 여성'**, **'30대 직장인'** 등 주요 페르소나에 따라 동일한 화면이 어떻게 다르게 렌더링되는지 보여주는 예시입니다.

| 페르소나 (Target Persona) | 화면 종류 (Screen Type) | 적용된 스타일 (Applied Style) |
|:---:|:---:|:---:|
| **10대 학생** | 로그인 화면 | ![](https://d41chssnpqdne.cloudfront.net/user_upload_by_module/chat_bot/files/2023169/wVzJaqGDJmeqY1VJ.jpg?Expires=1766235926&Signature=Tr1ce~7naBxIbEmPAq-PThOUqgl03T2KNqTdCfavIYymXjb6491dg8FWkOzCzpzDdqVv6qT2mtx-L3bxO0Ui5iCs~dDKjiuxPY4WU7LbTznITXVaFNoS1XVgKnjBpM7iJJx7~SGW4NGH9WNs9nY5IDg5KcjV6WXm7iio6Z-mHxWfyPMiqDU6672H2W99IK1GQAQRHowbnCyQkFbD-9yjeiGrv0xJcEYNk1filAlpCAgGyI6kWMBB41AhDYKxEigaFIxrCHq5NiGajaFuMu1ihFOJbzZsYUs8dyeI7d0YG3L4UJpkwwYLetOCDzavMnJHO1~1XNyc4qY3Yg4gnr4W6g__&Key-Pair-Id=K3USGZIKWMDCSX) |
| **20대 여성** | 음성 녹음 화면 | ![](https://d41chssnpqdne.cloudfront.net/user_upload_by_module/chat_bot/files/2023169/sO114HXrzlhyG3aA.jpg?Expires=1766235919&Signature=bLtJ96HEoy1unieHCr~qpL2HKuUSyY6qZec-VGyB~WJVkCw98TeRqZRsYw1HNvqlInhMkKwevy9Kd6F5Himq1hVPymc8ENaswHLyV33Rol5Sty-34UKEseDRqcKdq2ziZWKY0HCEDA9~0i-1gZU8ZpLlw6cFuepRubrBoH6gibUpzQg5Ph3x-wCy15V9SsXINk283RPuWtHONiEgAMx1Ebp~wbNtm5bLqk0CaiG-Oflib3Es9J5qx2by-FZ3Qpi8DeWxcyUo3MC6~L-x5pG~97BBgCvSG2E9fMB-B0LBxnc0-yhK5dZe8tc2xW6xsVIalzSA25jZVZe3zM0Tjp1vMQ__&Key-Pair-Id=K3USGZIKWMDCSX) |
| **30대 직장인** | 프로필 설정 화면 (승인) | ![](https://d41chssnpqdne.cloudfront.net/user_upload_by_module/chat_bot/files/2023169/dLnXZ8oCiCAj4Uj7.jpg?Expires=1766235922&Signature=Q1ekkXbVT7mO8BRtG10k-ZALs~qWgc8WQ6DFP1v7kyWQpJv2PUNzl8ruNxq~aQ7205r1pkVMswB74lBm8yR0mm4IYITO-vIC7inSBUdCCKcmaBTC6guUURfNYERi7bFHNFFYcdlRoUH~2oLRn7aVTAE4Bb-42vykE91jQno3kYs1TGLvPJ9r5JEDVchxO2cO-A9ag90XJFS84X~sdorZDBFKRn5R19igSawpT84B30fOz4cCt12feB5VOuAE3SZwDEwlDaS9EffoNXZ3ApZwY6dRPfZrhosfi-D9iQ8MWVnu2OL5RFHuJsHbDYt-ED5VWlYG8OIQNGERq4pGmkfCVQ__&Key-Pair-Id=K3USGZIKWMDCSX) |
| **30대 직장인** | 프로필 설정 화면 (기본) | ![](https://d41chssnpqdne.cloudfront.net/user_upload_by_module/chat_bot/files/2023169/50DyUz1JS6MbQ2WU.jpg?Expires=1766235925&Signature=WixpABoI7vrlrbKuurh~VnMB2yWJc7UJLLh-qO91X4zh82WFlgLpsYf-BNTgkGk1CW7cGzwaHIl2SUIOEVE3Rs8wx3MpkON-yyqa9sLmYnCfxdrbKZcE2hPzOilo7x0lXKLH40I~JSEYiimDiDnz03SlROBw6Wlaoyw0Nxa0bC46INdqOjK7camqKxSBjn9b1KJ80uoCYyRvjoSDPnGdZvWuZ2q1LFFFl1NvYivpFZpPcWbk4Yax~LI-zrKcthY253P-piato6xrBB9QeGjqZV49wylEmpJl7LE-RSoQIZ~q0bC4rUhHzZrkp3HUdZvg5hhHfjkIhABwpLdD5KwbxUg__&Key-Pair-Id=K3USGZIKWMDCSX) |

### 📤 최종 결과 내보내기 (Export Feature)

마음에 드는 스타일을 선택한 후, 비개발자도 쉽게 개발팀에 전달할 수 있도록 아래와 같은 형태로 결과물을 내보냅니다.

1.  **JSON Style Map Export**: 현재 화면에 적용된 모든 디자인 토큰(색상, 서체, 간격 등)을 담은 경량화된 JSON 파일 클립보드 복사.
2.  **Time Machine PDF Export**: 선택된 UI 화면의 비트맵 캡처 이미지와 함께 적용된 JSON 스타일 양식을 결합한 PDF 보고서 생성. (기획자/디자이너의 최종 승인 자료로 활용)

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
    *   프로젝트를 열고 `app` 모듈을 실행하면 즉시 'ComposeMarket'을 경험할 수 있습니다.

## 🔮 향후 로드맵 (Future Roadmap)

*   **JSON 개선**: 현재 색상을 바꾸는 것을 넘어 컴포넌트 수정도 가능하도록 개선.
*   **Git 추상화 개선**: 현재는 타임머신으로 git을 추상화했지만, 실제 타임머신 에니메이션 개발을 통해 더 직관적인 UI 제공.
*  **UI 마켓플레이스 확장**: 더 다양한 화면 제공 계획
*  **플랫폼 확장**: javascript, iOS 등 다른 플랫폼으로 확장 검토
*  **팀 단위 기능 제공 계획**: 팀 단위로 UI 스타일을 관리하고 공유할 수 있는 기능 추가 예정

## 📄 라이선스 (License)

본 프로젝트는 **Apache License 2.0** 하에 배포됩니다.