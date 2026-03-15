# GreenFit 🏋️‍♂️

![Kotlin](https://img.shields.io/badge/Kotlin-B125EA?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-4285F4?style=for-the-badge&logo=android&logoColor=white)
![Firebase](https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)

A modern Android application for managing fitness club activities, built entirely with Kotlin and Jetpack Compose. 

> **⚠️ DISCLAIMER:** This is an educational MVP (Minimum Viable Product) created solely for demonstration and learning purposes. This project is **NOT** affiliated with, endorsed by, or connected to any real fitness club named "GreenFit", including any establishments located in Brovary or elsewhere. All data, locations, and memberships are purely fictional.

## ✨ Key Features

- **Authentication & Profile:** Secure sign-up/sign-in using Firebase Auth. Includes user profile management with a dynamic, local custom avatar selection system.
- **Booking System:** View class schedules, filter by upcoming and past sessions, and cancel bookings with Optimistic UI updates for a seamless user experience.
- **Memberships:** Logic for validating, purchasing, and tracking the status of active fitness club memberships.
- **Customization:** Dynamic, on-the-fly theme switching (Light/Dark mode with custom palettes) and localization (English/Ukrainian) powered by Preferences DataStore.

## 🛠 Tech Stack

- **UI:** 100% Kotlin & Jetpack Compose (Material Design 3)
- **Backend:** Firebase Authentication, Cloud Firestore
- **Local Storage:** Preferences DataStore
- **Architecture:** Unidirectional Data Flow, State Management, Coroutines for asynchronous operations

## 📱 UI Showcase

https://github.com/user-attachments/assets/72f393cb-b879-41e1-9e47-c357b0e8b43b

### Application Screens
<p align="center">
  <img src="https://github.com/user-attachments/assets/2f7810ec-7201-49c1-92b7-8ca4bea639e2" width="220" />
  <img src="https://github.com/user-attachments/assets/529c47b5-ebb6-407a-8b9a-e8ff565db6da" width="220" />
  <img src="https://github.com/user-attachments/assets/c8dfab01-1929-4567-9634-e71b2dcfd6e6" width="220" />
</p>
<p align="center">
  <img src="https://github.com/user-attachments/assets/8a8e5c50-746d-41e7-b531-4dc24f7cc2b4" width="220" />
  <img src="https://github.com/user-attachments/assets/e10ed969-a27d-4857-acaa-1b2bebec4d31" width="220" />
  <img src="https://github.com/user-attachments/assets/c6a58cac-0302-42b2-a4be-8e9cf2fefcc4" width="220" />
</p>

## 🚀 Getting Started

To build and run this project locally:

1. Clone the repository:
   ```bash
   git clone https://github.com/ravion223/GreenFit.git
   ```
2. Open the project in Android Studio.

3. Important: This project uses Firebase. You must create your own Firebase project, enable Authentication and Firestore, and add your google-services.json file to the app/ directory. This file is not included in the repository for security reasons.

4. Build and run on an emulator or physical device.
