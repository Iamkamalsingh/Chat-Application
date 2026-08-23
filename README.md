# 💬 LooP — Chat Application

An Android chat application built with **Firebase** for real-time messaging and authentication.

## ✨ Features

- 🔐 User registration & login (Firebase Auth)
- 💬 Real-time messaging (Firebase Realtime Database)
- 🖼️ Media sharing via Firebase Storage
- 👤 User profiles with avatar support
- 🎨 Clean, intuitive UI with custom fonts & icons

---

## 📁 Project Structure

```
Chat-Application/              ← GitHub repo root
├── .gitignore                 ← Root-level ignore rules
├── Jenkinsfile                ← CI/CD pipeline (Jenkins)
├── README.md
├── Resources/
│   ├── font/                  ← Source font assets
│   └── icon/                  ← App icon source files
└── Loop/                      ← Android project
    ├── gradlew / gradlew.bat  ← Gradle wrapper
    ├── build.gradle.kts       ← Root Gradle build
    ├── settings.gradle.kts
    ├── gradle.properties
    ├── gradle/
    │   ├── libs.versions.toml ← Dependency version catalog
    │   └── wrapper/
    └── app/                   ← App module
        ├── build.gradle.kts
        ├── proguard-rules.pro
        └── src/
            ├── main/
            │   ├── AndroidManifest.xml
            │   ├── java/com/singhkamal/loop/
            │   └── res/
            ├── test/
            └── androidTest/
```

---

## 🚀 Local Setup

### Prerequisites
- Android Studio (latest stable)
- JDK 21
- Android SDK (API 34)

### Steps

1. **Clone the repo**
   ```bash
   git clone https://github.com/<your-username>/Chat-Application.git
   cd Chat-Application
   ```

2. **Add `google-services.json`**
   - Download from your [Firebase Console](https://console.firebase.google.com/)
   - Place at: `Loop/app/google-services.json`
   - ⚠️ This file is gitignored — never commit it

3. **Open in Android Studio**
   - Open the `Loop/` folder as the project root

4. **Build & Run**
   ```bash
   cd Loop
   ./gradlew assembleDebug
   ```

---

## 🏗️ CI/CD with Jenkins

The project includes a `Jenkinsfile` at the repo root that automates the build pipeline.

### Pipeline Stages

| Stage | Description |
|---|---|
| **Checkout** | Pulls latest code from GitHub |
| **Inject google-services.json** | Copies Firebase config from Jenkins credentials |
| **Check Environment** | Verifies JDK and Gradle versions |
| **Build Debug APK** | Runs `gradlew clean assembleDebug` |
| **Archive APK** | Saves the APK as a Jenkins build artifact |

### Jenkins Setup

1. **Install Jenkins** and the following plugins:
   - Git Plugin
   - Pipeline Plugin
   - Credentials Binding Plugin

2. **Add Firebase secret:**
   - Go to **Manage Jenkins → Credentials → Global → Add Credentials**
   - Kind: **Secret file**
   - ID: `google-services-json`
   - Upload your `google-services.json` file

3. **Create a Pipeline job:**
   - New Item → Pipeline
   - Pipeline definition: **Pipeline script from SCM**
   - SCM: Git → your repo URL
   - Branch: `Dev` (or `main`)
   - Script Path: `Jenkinsfile`

4. **Ensure the Jenkins agent has:**
   - JDK 21 configured as `JDK21` in Global Tool Configuration
   - Android SDK at `C:\Users\ASUS\AppData\Local\Android\Sdk`

### Triggering a Build

- **Manually:** Click **Build Now** in Jenkins
- **Automatically:** Configure a GitHub webhook:
  - Payload URL: `http://<jenkins-url>/github-webhook/`
  - Events: Push, Pull Request

---

## 🔒 Security Notes

- `google-services.json` is **gitignored** — injected at CI/CD build time via Jenkins Secret File
- Never commit API keys, keystores, or `.jks` files
- `local.properties` is gitignored (contains local SDK paths)

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java |
| UI | XML Layouts, Material Design |
| Auth | Firebase Authentication |
| Database | Firebase Realtime Database |
| Storage | Firebase Storage |
| Image Loading | Picasso |
| UI Utilities | Intuit SDP / SSP, CircleImageView |

---

## 📄 License

This project is for educational purposes.
