plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    kotlin("kapt")
}

android {
    namespace = "cl.duoc.levelappchile"
    compileSdk = 35

    defaultConfig {
        applicationId = "cl.duoc.levelappchile"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            // Configs debug opcionales
        }
    }

    // Habilita Compose
    buildFeatures { compose = true }
    composeOptions { kotlinCompilerExtensionVersion = "1.5.15" }

    // Java/Kotlin 17
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }

    // Evita conflictos de licencias
    packaging { resources.excludes += "/META-INF/{AL2.0,LGPL2.1}" }
}

/* Forzamos repos también a nivel módulo por si el IDE ignora los del settings */
repositories {
    google()
    mavenCentral()
}

dependencies {
    // --------- Jetpack Compose (BOM mantiene versiones compatibles) ----------
    implementation(platform("androidx.compose:compose-bom:2024.10.01"))
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.navigation:navigation-compose:2.8.3")
    implementation("androidx.compose.foundation:foundation:1.7.4")
    implementation("androidx.compose.animation:animation:1.7.4")
    implementation("io.coil-kt:coil-compose:2.7.0")

    // --------- Firebase (BoM + SDKs) ----------
    implementation(platform("com.google.firebase:firebase-bom:34.4.0"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-storage-ktx")

    // --------- AndroidX / utilidades ----------
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")

    // Localización (GPS) si la usarás
    implementation("com.google.android.gms:play-services-location:21.3.0")

    // ViewModel/Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")

    // Permisos (opcional)
    implementation("com.google.accompanist:accompanist-permissions:0.36.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")
}