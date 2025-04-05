# 🐾 Zviryata – A Game for the Little Ones

**Zviryata** is a colorful, interactive Android game built with **Kotlin** and **Jetpack Compose**, designed specifically for **toddlers and very young children**. 
The game helps develop motor skills and auditory recognition through playful interactions with cute animals, animations, and sounds.

---

## 🎯 Highlights

- 🧒 Simple, child-friendly gameplay for ages 1–4  
- 🐶 6 unique levels with animal images, sounds, and background scenes  
- ✨ Smooth animations and feedback for every tap  
- ✅ Levels saved using SharedPreferences  
- 🧠 Clean MVVM architecture  
- 💉 Hilt for dependency injection  
- 📱 100% Jetpack Compose UI – no XML  
- 📦 Modular structure with shared logic (`:shared` module)  
- 🎞️ Lottie animations for engaging effects  
- 📈 AdMob integration: banner + interstitial ads  
- 🔒 Min SDK 24 / Target SDK 35

---

## 🔧 Tech Stack

- **Language:** Kotlin  
- **UI:** Jetpack Compose, Material 3  
- **Architecture:** MVVM + Hilt  
- **Animation:** Lottie Compose  
- **Persistence:** SharedPreferences  
- **Ads:** Google AdMob  
- **Testing:** JUnit, Espresso, Compose UI tests  
- **Multiplatform Ready:** Includes `:shared` module for future KMP support

---

## 🛠️ How to Build

1. Clone the repository:

   ```bash
   git clone https://github.com/AntonMuzhytskyi/zviryata_game.git
   cd zviryata_game
2. Open the project in Android Studio.  
3. Sync the project with Gradle files. 
5. Build and run the app on an emulator or physical device.  

### Building a Release APK
- Configure the signing settings in `app/build.gradle` (see `signingConfigs`).  
- Run `./gradlew assembleRelease` to generate a signed APK.    

## 📦 Download APK

Want to try the game without building it yourself?

👉 [**Download Zviryata v1.1 (APK)**](https://github.com/AntonMuzhytskyi/zviryata_game/releases/download/v.1.1/zviryata.apk)

> Compatible with Android 7.0 (API 24) and up.


## About Me
This project was built as a portfolio piece to demonstrate my skills as an indie Android developer. It reflects my passion for clean UI, architecture, and joyful user experience.

Feel free to explore the code or reach out if you're looking for a Kotlin/Android developer who cares about detail and UX! 🚀  

Feel free to contact me to discuss potential opportunities or explore the code!
