import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.vanniktech.mavenPublish)
    alias(libs.plugins.kotlinCocoapods)
}

group = "io.github.seanchinjunkai"
version = "0.1.0"

kotlin {
    androidTarget {
        publishLibraryVariants("release")
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        ios.deploymentTarget = libs.versions.ios.deploymentTarget.get()
        framework {
            baseName = "FirebaseAI" // Not FirebaseAIBridge due to ld: can't link a dylib with itself. same install_name as dylib being built
        }
        pod("FirebaseAIBridge") {
            source = git("https://github.com/SeanChinJunKai/FirebaseAIBridge.git") {
                branch = "main"
            }
            extraOpts += listOf("-compiler-option", "-fmodules")
        }
    }

    sourceSets {

        val commonMain by getting {
            dependencies {
                implementation(libs.kermit)
                implementation(libs.kotlinx.coroutines)
                implementation(libs.kotlinx.datetime)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(project.dependencies.platform(libs.firebase.bom))
                implementation(libs.firebase.ai)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.kotlinx.coroutines.test)
            }
        }
    }
}

android {
    namespace = "io.github.seanchinjunkai.firebase_ai_kmp"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    if (!gradle.startParameter.taskNames.any { it.contains("publishToMavenLocal") }) {
        signAllPublications()
    }

    coordinates(group.toString(), "firebase-ai-kmp", version.toString())

    pom {
        name = "Firebase Vertex AI KMP"
        description = "A Kotlin Multiplatform Library for Firebase Vertex AI"
        inceptionYear = "2025"
        url = "https://github.com/SeanChinJunKai/firebase-ai-kmp.git"
        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                distribution = "https://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }
        developers {
            developer {
                id = "SeanChinJunKai"
                name = "Sean Chin Jun Kai"
                url = "https://github.com/SeanChinJunKai"
            }
        }
        scm {
            url = "https://github.com/SeanChinJunKai/firebase-ai-kmp.git"
            connection = "scm:git:git//github.com/SeanChinJunKai/firebase-ai-kmp.git"
            developerConnection = "scm:git:ssh://git@github.com/SeanChinJunKai/firebase-ai-kmp.git"
        }
    }
}
