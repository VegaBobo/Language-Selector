plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.com.google.dagger.hilt)
    alias(libs.plugins.com.mikepenz.aboutlibraries)
    alias(libs.plugins.compose.compiler)
    kotlin("kapt")
}

android {
    namespace = "vegabobo.languageselector"
    compileSdk = 35

    defaultConfig {
        applicationId = "vegabobo.languageselector"
        minSdk = 33
        targetSdk = 35
        versionCode = 5
        versionName = "1.04"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
    buildFeatures {
        buildConfig = true
        compose = true
        aidl = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

kapt {
    correctErrorTypes = true
}

aboutLibraries {
    excludeFields = arrayOf("generated")
}

dependencies {
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    implementation(libs.libsu.core)
    implementation(libs.libsu.service)

    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material)
    implementation(libs.material3)

    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    implementation(libs.aboutlibraries.core)

    implementation(libs.shizuku.api)
    implementation(libs.shizuku.provider)

    implementation(libs.hiddenapibypass)

    compileOnly(project(":hidden_api"))
}