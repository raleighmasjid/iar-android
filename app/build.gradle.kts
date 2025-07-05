import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.jetbrains.kotlin.compose)
    alias(libs.plugins.kotlinX.serialization)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.madinaapps.iarmasjid"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.madinaapps.iarmasjid"
        minSdk = 23
        targetSdk = 35
        versionCode = 239
        versionName = "3.2.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            ndk {
                debugSymbolLevel = "full"
            }
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.work.ktx)
    implementation(libs.onesignal)
    implementation(libs.core.ktx)
    implementation(libs.bundles.compose)
    implementation(libs.navigation.compose)
    implementation(libs.bundles.lifecycle)
    implementation(libs.activity.compose)
    implementation(libs.bundles.accompanist)
    implementation(libs.bundles.retrofit)
    implementation(libs.bundles.moshi)
    implementation(libs.datastore.preferences)
    implementation(libs.dagger.hilt)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.coil.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.play.services.location)
    implementation(libs.adhan)
    implementation(libs.splashscreen)
    implementation(libs.google.maps)
    ksp(libs.hilt.android.compiler)
    ksp(libs.moshi.kotlin.codegen)
    coreLibraryDesugaring(libs.desugar.jdk.libs)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.compose.test.junit4)
    debugImplementation(libs.compose.ui.tooling)
}

secrets {
    propertiesFileName = "secrets.properties"
}