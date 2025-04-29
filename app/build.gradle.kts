plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.kidsplay"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.kidsplay"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.recyclerview)
    implementation(libs.cardview)
    implementation(libs.volley)
    implementation(libs.swiperefreshlayout)

    // Test dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // MySQL Connector
    implementation(libs.mysql.connector.java.v5149)

    // Image loading
    implementation(libs.glide)
    annotationProcessor(libs.glide.compiler)
    implementation(libs.picasso) // Consider using either Glide or Picasso, not both

    // Network and JSON
    implementation(libs.gson)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // Media player - choose only one version
    implementation(libs.media3.exoplayer.v131)
    implementation(libs.media3.ui.v131)

    // Database
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)

    // Logging
    implementation(libs.timber)

    // Color Picker
    implementation(libs.yukuku.ambilwarna)
}