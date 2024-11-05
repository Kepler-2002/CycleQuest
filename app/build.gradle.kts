plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.cyclequest"
    compileSdk = 34
    
    defaultConfig {
        applicationId = "com.cyclequest"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        
        ndk {
            abiFilters += listOf("armeabi-v7a", "arm64-v8a","x86_64")
        }
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
            all {
                it.maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).coerceAtLeast(1)
                it.forkEvery = 100
                it.maxHeapSize = "1024m"
                it.testLogging {
                    events("passed", "skipped", "failed", "standardOut", "standardError")
                    showStandardStreams = true
                    showExceptions = true
                    showCauses = true
                    showStackTraces = true
                }
            }
        }
    }

    
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    
    kotlinOptions {
        jvmTarget = "17"
        // freeCompilerArgs += listOf("-P", "plugin:androidx.compose.compiler.plugins.kotlin:suppressKotlinVersionCompatibilityCheck=true")
    }

    buildFeatures {
        compose = true
        viewBinding = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    sourceSets {
        getByName("test") {
            java.srcDirs("src/test/java")
        }
    }
}

dependencies {
    val roomVersion = "2.6.1"
    
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    
    // Compose
    val composeBomVersion = "2023.08.00"
    implementation(platform("androidx.compose:compose-bom:$composeBomVersion"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.48")
    implementation("androidx.navigation:navigation-compose:2.8.2")
    implementation("com.google.firebase:firebase-crashlytics-buildtools:3.0.2")
    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("com.google.android.gms:play-services-basement:18.4.0")
    implementation("androidx.test.ext:junit-ktx:1.2.1")
    implementation("com.android.volley:volley:1.2.1")
    implementation("com.google.firebase:firebase-database-ktx:21.0.0")
    implementation("androidx.databinding:adapters:3.2.0-alpha11")
    implementation("androidx.work:work-runtime-ktx:2.9.1")
    kapt("com.google.dagger:hilt-android-compiler:2.48")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    
    // 高德地图
    implementation("com.amap.api:map2d:latest.integration")
    implementation("com.amap.api:location:latest.integration")
    
    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test:rules:1.5.0")
    androidTestImplementation("androidx.test.ext:junit-ktx:1.1.5")

    // 添加 OkHttp
    implementation("com.squareup.okhttp3:okhttp:4.10.0")

    // 添加 EventBus
    implementation("org.greenrobot:eventbus:3.2.0")

    // 添加 SLF4J 和 Logback
    implementation("org.slf4j:slf4j-api:1.7.32")
    implementation("ch.qos.logback:logback-classic:1.2.6")

    // 添加 Timber
    implementation("com.jakewharton.timber:timber:5.0.1")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.code.gson:gson:2.8.9")

    testImplementation("org.mockito:mockito-core:3.12.4")
    testImplementation("org.mockito.kotlin:mockito-kotlin:3.2.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.9.0")
    // testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:1.9.0")

    implementation("com.google.accompanist:accompanist-permissions:0.28.0")

    // Room 测试依赖
    testImplementation("androidx.room:room-testing:2.6.1")
    
    // AndroidX 测试依赖
    testImplementation("androidx.test:core:1.5.0")
    testImplementation("androidx.test:runner:1.5.0")
    testImplementation("androidx.test.ext:junit:1.1.5")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    
    // Kotlin 协程测试
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")
    
    // Mock 测试框架
    testImplementation("io.mockk:mockk:1.13.5")
    testImplementation("com.google.truth:truth:1.1.5")
    
    // Robolectric - Android 单元测试框架
    testImplementation("org.robolectric:robolectric:4.10.3")
    
    // JUnit 5
    // testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    // testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    // testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.0")
}

kapt {
    correctErrorTypes = true
}
