# Firebase AI Logic Kotlin Multiplatform (KMP) SDK

> ⚠️ : This is an experimental library and may be subject to breaking changes.

Build AI-powered mobile and web apps and features with the Gemini and Imagen models using Firebase AI Logic

## Table of Contents

1. [Supported Platforms](#supported-platforms)
2. [Getting Started](#getting-started)
3. [Samples](#try-out-the-sample-app-powered-by-this-sdk)

## Supported Platforms
| Platform | Targets Supported                     |
|----------|----------------------------------------|
| Android  | `androidTarget`                        |
| iOS      | `iosArm64`, `iosX64`, `iosSimulatorArm64` |

## Getting Started
### Requirements
- A Kotlin Multiplatform project with both Android and iOS targets

### Project Setup
1. Add the dependency
    - Add the following to your `libs.versions.toml`:
    ```
    [versions]
    firebase-ai-kmp = "0.3.0"
    
    [libraries]
    firebase-ai-kmp = { module = "io.github.seanchinjunkai:firebase-ai-kmp", version.ref = "firebase-ai-kmp" }
    ```
    - Then, add the dependency in your `commonMain` source set:
    ```
    commonMain.dependencies {
        implementation(libs.firebase.ai.kmp)
    }
    ```

2. Firebase Console Setup
    - Go to the [Firebase Console](https://console.firebase.google.com/).
    - Create a new project if you haven't already.
    - In your project, navigate to the left sidebar and click on `AI` -> `AI Logic` then open `Settings`.
    - Enable either the Gemini Developer API, the VertexAI Gemini API, or both.
> **_NOTE:_** Using the Vertex AI Gemini API requires that your project is linked to a Cloud Billing account. This means that your Firebase project is on the pay-as-you-go Blaze pricing plan.

3. Link the native [iOS Bridge](https://github.com/SeanChinJunKai/FirebaseAIBridge)

   Since the SDK depends on a native Firebase iOS framework, FirebaseAIBridge, this will need to be linked to your existing iOS project.

4. Using CocoaPods
    1. Directly as a local iOS framework
        - Follow these instructions if your Kotlin Multiplatform module is integrated directly with your iOS project as a local iOS framework. That is, Xcode is calling the embedAndSignAppleFrameworkForXcode Gradle task as part of the build. At the time of writing, if you generated your Kotlin Multiplatform project using the [online wizard](https://kmp.jetbrains.com/), this is how it's set up.
        - In this scenario, you need to specify the transitive dependency on FirebaseAIBridge in your Podfile. Add the following:
          ```
          pod 'FirebaseAIBridge', :git => 'https://github.com/SeanChinJunKai/FirebaseAIBridge.git', :tag => '0.3.0'
          ```

6. iOS Setup
    - Create an iOS app in your Firebase project.
    - Download `GoogleService-Info.plist` and place it under `iosApp/iosApp` directory.
    - Add the FirebaseCore depedency to your `Podfile`:
    ```
    pod 'FirebaseCore', :git => 'https://github.com/firebase/firebase-ios-sdk.git', :tag => '11.15.0'
    ```
    - Add the following initialization code in `iosApp/iosApp/iOSApp.swift`
    ```
    import SwiftUI
    import FirebaseCore
    
    
    class AppDelegate: NSObject, UIApplicationDelegate {
      func application(_ application: UIApplication,
                       didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        FirebaseApp.configure()
    
        return true
      }
    }
    
    @main
    struct YourApp: App {
      // register app delegate for Firebase setup
      @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate
    
    
      var body: some Scene {
        WindowGroup {
          NavigationView {
            ContentView()
          }
        }
      }
    }
    ```
    - Run pod install in the `iosApp` directory

7. Android Setup
    - Create an Android app in the Firebase Console.
    - Download `google-services.json` and place it in `composeApp` directory.
    - Add the following to your `libs.versions.toml`:
    ```
    [versions]
    googleServices = "4.4.3"
    
    [plugins]
    googleServices = { id = "com.google.gms.google-services", version.ref = "googleServices" }
    ```
    - In the root `build.gradle.kts` add the plugin:
    ```
    plugins {
        alias(libs.plugins.googleServices) apply false
    }
    ```
    - In `composeApp/build.gradle.kts`, add the plugin:
    ```
    plugins {
        alias(libs.plugins.googleServices)
    }
    ```

8. Import FirebaseAI

   You should now be able to import `FirebaseAI` in `commonMain`
    ```
    import io.github.seanchinjunkai.firebase.ai.Firebase
    import io.github.seanchinjunkai.firebase.ai.GenerativeBackendEnum
    ```


9. Quick start
```
// Initialize the Gemini Developer API backend service
// Create a `GenerativeModel` instance with a model that supports your use case
val model = Firebase.ai(GenerativeBackend.googleAI()).generativeModel("gemini-2.0-flash")

val prompt = "Write a story about a magic backpack."
val response = model.generateContent(prompt)
print(response.text)
```

## Try out the sample app powered by this SDK
[firebase-ai-sample](https://github.com/SeanChinJunKai/firebase-ai-sample) - Kotlin Multiplatform sample application that showcases Firebase AI integration. Runs on both Android and iOS
