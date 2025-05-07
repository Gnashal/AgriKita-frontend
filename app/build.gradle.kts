import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

val var1= gradleLocalProperties(rootDir, providers)
    .getProperty("OPENWEATHER_API_KEY", "")
val var2= gradleLocalProperties(rootDir, providers)
    .getProperty("NEWS_API_KEY", "")

android {
    namespace = "mobdev.agrikita"
    compileSdk = 35

    defaultConfig {
        applicationId = "mobdev.agrikita"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        resValue(
            "string",
            "OPENWEATHER_API_KEY",
                "\"" + var1 + "\""
        )
        resValue(
            "string",
            "NEWS_API_KEY",
            "\"" + var2 + "\""
        )
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
    implementation(libs.swiperefreshlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.github.bumptech.glide:glide:4.15.1") // or the latest version
    annotationProcessor ("com.github.bumptech.glide:compiler:4.15.1")
//    implementation("com.github.KwabenBerko:News-API-Java:1.0.2")
    implementation ("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation(platform("com.google.firebase:firebase-bom:33.12.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation ("com.squareup.okhttp3:okhttp:4.9.3")
    implementation ("androidx.cardview:cardview:1.0.0")
}