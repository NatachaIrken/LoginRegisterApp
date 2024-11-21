plugins {
    id("com.android.application") version "8.6.0" apply true
    id("org.jetbrains.kotlin.android") version "1.9.10" apply true
}

android {
    namespace = "com.example.notasapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.notasapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
}
