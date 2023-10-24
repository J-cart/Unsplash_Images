plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("androidx.navigation.safeargs")
    id ("kotlin-parcelize")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.tutorials.unsplashimages"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.tutorials.unsplashimages"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures{
        viewBinding = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    val nav_version = "2.7.4"

// Kotlin
    implementation ("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation ("androidx.navigation:navigation-ui-ktx:$nav_version")
    // Retrofit with Moshi Converter
    implementation ("com.squareup.retrofit2:converter-moshi:2.9.0")

    // retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    // define a BOM and its version
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.9.3"))

    // define any required OkHttp artifacts without version
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")

    //Moshi
    implementation ("com.squareup.moshi:moshi-kotlin:1.13.0")
    // Coil
    implementation ("io.coil-kt:coil:2.2.2")

    val room_version = "2.6.0"
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation ("androidx.room:room-ktx:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    // optional - Paging 3 Integration
    implementation ("androidx.room:room-paging:$room_version")

    //dagger hilt
    implementation ("com.google.dagger:hilt-android:2.46")
    kapt("com.google.dagger:hilt-android-compiler:2.46")

    //paging3
    implementation("androidx.paging:paging-runtime:3.2.1")

    //swipe to refresh
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")


    //kotlin main coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")


    val lifecycle_version = "2.6.2"
    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    // Lifecycles only (without ViewModel or LiveData)
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")

}
kapt {
    correctErrorTypes = true
}