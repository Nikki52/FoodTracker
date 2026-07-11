# Food Tracker 🥗

![Build Status](https://img.shields.io/badge/build-passing-brightgreen)
![Platform](https://img.shields.io/badge/platform-Android-3DDC84)
![Language](https://img.shields.io/badge/language-Kotlin-7F52FF)
![License](https://img.shields.io/badge/license-MIT-blue)

## Description

**Food Tracker** is a **completely offline** Android application built to track daily nutrition, calories, and macros. It features a comprehensive, pre-populated Indian food database with over 1,300+ authentic Indian dishes, automatically mapping combinations like "Tomato Curry with 2 Chapatis". Powered by Kotlin, Jetpack Compose, and Room Database, it offers a fast, fluid, and intuitive way to stay on top of your dietary goals without needing an internet connection. All data is stored locally on your device.

## Table of Contents
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [Running on a Physical Mobile Device](#running-on-a-physical-mobile-device)
- [Adding More Custom Food Data (CSV Import)](#adding-more-custom-food-data-csv-import)
- [Usage](#usage)
- [Troubleshooting](#troubleshooting)

## Prerequisites

Before you begin, ensure you have the following installed:
- **Java Development Kit (JDK) 17** or higher
- **Android SDK** (API 34 or higher recommended)
- **Android Studio** (Recommended) or **Visual Studio Code** (with Android & Kotlin extensions)
- Python 3 (only if you want to import custom `.csv` data)

## Installation & Setup

1. **Open the project in VS Code or Android Studio:**
   Navigate to the root directory of the project (`remix_-food-tracker`).

2. **Generate a Debug Keystore:**
   Since you downloaded the project, the `debug.keystore` file is not included by default. **You must generate it in the root folder of the project** before building. 
   Open your terminal (in VS Code, make sure you are at the project root) and run:
   ```bash
   keytool -genkey -v -keystore debug.keystore -storepass android -alias androiddebugkey -keypass android -keyalg RSA -keysize 2048 -validity 10000 -dname "C=US, O=Android, CN=Android Debug"
   ```
   *(Note: Ensure `keytool` is in your system PATH. It is included with your JDK installation).*

3. **Verify the Keystore:**
   Ensure `debug.keystore` is now visible in the root folder (next to `gradlew`, `build.gradle.kts`, etc.).

## Running on a Physical Mobile Device

To run and update the app directly on your physical Android phone using VS Code:

1. **Enable Developer Options & USB Debugging:**
   - On your phone, go to **Settings > About Phone**.
   - Tap **Build Number** 7 times to unlock Developer Options.
   - Go to **Settings > System > Developer Options** and enable **USB Debugging**.

2. **Connect Your Phone:**
   Connect your phone to your computer via a USB cable. If prompted on your phone to "Allow USB debugging", tap **Allow**.

3. **Verify Connection:**
   In your VS Code terminal, run:
   ```bash
   adb devices
   ```
   You should see your device listed (e.g., `device_id    device`).

4. **Compile and Install the App:**
   In the VS Code terminal (project root), run:
   **Windows (PowerShell/CMD):**
   ```cmd
   .\gradlew.bat installDebug
   ```
   **Mac/Linux:**
   ```bash
   ./gradlew installDebug
   ```
   *This command will build the app and install/update it on your connected phone.*

5. **Launch the App:**
   Open the "Food Tracker" app from your phone's app drawer.

## Adding More Custom Food Data (CSV Import)

The project includes an intelligent data importer script (`import_data.py`) which automatically scans for **all `.csv` files** placed in the root directory.

If you want to add more bulk food data and **update the app on your phone**:

1. Drop your `.csv` file(s) into the root project directory.
2. The script intelligently looks for the following column names (case-insensitive where possible):
   - **Name:** `Dish Name`, `food_name`, or `Name`
   - **Calories:** `Calories (kcal)`, `energy_kcal`, or `Calories`
   - **Carbs:** `Carbohydrates (g)`, `carb_g`, or `Carbs`
   - **Protein:** `Protein (g)`, `protein_g`, or `Protein`
   - **Fats:** `Fats (g)`, `fat_g`, or `Fat`
   - **Fiber:** `Fibre (g)`, `fibre_g`, or `Fiber`
3. Run the script from the root directory:
   ```bash
   python import_data.py
   ```
4. This will automatically parse all CSVs, prevent duplicates, and compile them into `app/src/main/assets/external_foods.json`.
5. **Update the app on your phone:**
   Ensure your phone is connected and run:
   ```cmd
   .\gradlew.bat installDebug
   ```
6. **Important:** When the app launches, it will intelligently detect the new foods and re-import them. **Your previously logged meals and water entries will be completely preserved!** You do NOT need to clear app data or reinstall. It acts as a seamless update.

## Usage

- **Dashboard:** View your daily calorie consumption and macro breakdown (Protein, Carbs, Fats) at a glance. Includes water tracking.
- **Logging Food:** Tap the "+" button to search for foods. The app includes thousands of standard and Indian meals.
- **Water Tracking:** Track your daily water intake with the water widget, and undo accidental entries if needed.
- **Custom Foods:** Create your own foods with custom macro profiles if they aren't available in the pre-loaded dataset.

## Troubleshooting

- **"Keystore file 'debug.keystore' not found" error:**
  You skipped Step 2 in the Installation setup. Run the `keytool` command in the root of your project directory (the same directory that contains `gradlew.bat`), then run `.\gradlew.bat --stop` and try building again.
- **"SDK location not found" error:**
  Make sure the `ANDROID_SDK_ROOT` or `ANDROID_HOME` environment variable is set. In VS Code, you can add this to your terminal profile or system variables.
- **`adb` or `keytool` is not recognized:**
  You need to add the Android SDK `platform-tools` folder (for `adb`) and the JDK `bin` folder (for `keytool`) to your system's PATH environment variables.
