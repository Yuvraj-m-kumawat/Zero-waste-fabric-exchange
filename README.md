# KutiraKone: Hyper-local Fabric Scrap Marketplace

## 📌 Problem Statement
Home-based tailors and boutique owners often generate excess fabric scraps that go to waste. Simultaneously, other artisans and hobbyists require small pieces of fabric for creative projects, but there is no structured way to trade or exchange these materials locally.

## 🎯 Project Objective
To develop a hyper-local mobile application that enables users to upload, browse, and exchange leftover fabric scraps. KutiraKone promotes reuse, reduces textile waste, and encourages a circular economy among local artisans.

## 🚀 Features
*   **Fabric Upload**: Easily add fabric scraps with clear images, precise size/quantity, and material category.
*   **Catalog Browsing**: A visually intuitive Material 3 grid view to browse all available local scraps.
*   **Smart Filtering**: Filter listings by material type (Silk, Cotton, Wool, Denim, etc.) to find exactly what you need.
*   **Hyper-Local Discovery**: Radius-based search to find fabric scraps within a 5 km radius of your current location.
*   **Multilingual Support**: Fully localized UI with instant switching between **English, Hindi (हिंदी), and Kannada (ಕನ್ನಡ)**.
*   **AI Smart Stylist**: Integrated with **Google Gemini AI** to provide creative upcycling suggestions based on the fabric type.
*   **Buy or Swap Requests**: Real-time request system allowing users to buy or swap materials with other artisans.
*   **Real-time Sync**: Powered by Firestore to ensure new listings appear instantly across all devices.

## 🛠 Tech Stack
*   **Frontend**: Native Android with **Kotlin**
*   **UI Framework**: **Jetpack Compose** (Modern Declarative UI)
*   **Design System**: **Material 3 (M3)**
*   **Backend**: **Firebase Cloud Firestore** (Real-time Database)
*   **Storage**: **Firebase Storage** (Image Hosting)
*   **AI Engine**: **Google Gemini AI** (LLM Integration)
*   **Geo-Services**: Google Play Services Location
*   **Concurrency**: Kotlin Coroutines & StateFlow
*   **Image Loading**: Coil

## 📂 Project Structure
```text
com.example.kutirakone/
├── model/         # Data classes (Scrap, User, Request)
├── viewmodel/     # State management and UI Logic
├── repository/    # Firebase & Data access layer
├── ui/
│   ├── screens/   # Compose screens (Home, Listings, Upload, AI)
│   └── components/# Reusable UI elements (ScrapCards, RequestCards)
├── service/       # Background services (FCM Messaging)
└── util/          # Helper classes (Location, Validations)
```

## ⚙️ Installation & Setup
1.  **Clone the Repository**:
    ```bash
    git clone https://github.com/your-username/KutiraKone.git
    ```
2.  **Firebase Setup**:
    *   Create a Firebase project at [Firebase Console](https://console.firebase.google.com/).
    *   Enable **Firestore**, **Storage**, and **Anonymous/Phone Auth**.
    *   Download your `google-services.json` and place it in the `app/` directory.
3.  **Gemini AI Setup**:
    *   Get an API key from [Google AI Studio](https://aistudio.google.com/).
    *   Add your key to `AiSuggestActivity.kt` (or use local.properties).
4.  **Build**:
    *   Open the project in **Android Studio**.
    *   Sync Gradle and click **Run** (Play button) on your emulator or physical device.

## 📸 Screenshots
*(Add your app screenshots to the `/screenshots` folder and link them here)*

---
*Developed for the Automated Project Evaluation - Aiming for Excellence.*
