buildscript {
    dependencies {
        classpath(libs.hilt.android.gradle.plugin)
    }
}

plugins {
    alias(libs.plugins.android.application).apply(false)
    alias(libs.plugins.android.library).apply(false)
    alias(libs.plugins.jetbrains.kotlin.android).apply(false)
    alias(libs.plugins.ksp).apply(false)
    alias(libs.plugins.jetbrains.kotlin.compose).apply(false)
    alias(libs.plugins.kotlinX.serialization).apply(false)
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}