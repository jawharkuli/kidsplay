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
    implementation(libs.media3.ui)
    implementation(libs.media3.exoplayer)
    implementation(libs.swiperefreshlayout)

    // Test dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // ✅ MySQL Connector (Ensure correct version is used)
    implementation(libs.mysql.connector.java.v5149)

    // Additional dependencies
    implementation(libs.glide)
    annotationProcessor(libs.glide.compiler)
    implementation(libs.gson)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // ✅ Color Picker Libraries (Choose One)
    implementation("com.github.yukuku:ambilwarna:2.0.1")
}
