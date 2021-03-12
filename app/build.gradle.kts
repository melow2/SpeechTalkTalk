import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlinVersion: String by rootProject.extra
val navVersion: String by rootProject.extra
val coroutinesVersion: String by rootProject.extra
val lifecycleVersion: String by rootProject.extra
val pagingVersion: String by rootProject.extra
val koinVersion: String by rootProject.extra
val materialVersion: String by rootProject.extra
val glideVersion: String by rootProject.extra
val rxBindingVersion: String by rootProject.extra
val timberVersion: String by rootProject.extra
val rxRelayVersion: String by rootProject.extra
val threetenabpVersion: String by rootProject.extra
val retrofit2Version: String by rootProject.extra
val roomVersion: String by rootProject.extra
val workVersion: String by rootProject.extra

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
}

// apply(plugin = "com.google.gms.google-services")
// apply(plugin = "com.google.firebase.crashlytics")

android {
    compileSdkVersion(30)

    signingConfigs {
        create("release") {
            keyAlias = "kakaopay"
            keyPassword = "kakaopay"
            storeFile = file("../kakaopay.jks")
            storePassword = "kakaopay"
        }
    }

    defaultConfig {
        applicationId = "com.khs.coconut"
        minSdkVersion(23)
        targetSdkVersion(30)
        versionCode = 2
        versionName = "2.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = false
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
            )
            applicationVariants.all {
                outputs
                        .map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }
                        .forEach { output ->
                            output.outputFileName = output.outputFileName
                                    .replace("app-", "kakao_pay_")
                                    //.replace(".apk", "-${variant.versionName}.${variant.versionCode}.apk")
                                    .replace(".apk", "_${versionName}.apk")
                        }
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        dataBinding = true
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = freeCompilerArgs + listOf("-Xopt-in=kotlin.RequiresOptIn")
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // Kotlin, Kotlin Coroutines
    implementation(kotlin("stdlib-jdk8", kotlinVersion))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-rx3:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:$coroutinesVersion")

    // AndroidX, Material
    implementation("androidx.recyclerview:recyclerview:1.2.0-beta02")
    implementation("androidx.appcompat:appcompat:1.3.0-beta01")
    implementation("androidx.core:core-ktx:1.5.0-beta02")
    implementation("androidx.activity:activity-ktx:1.3.0-alpha03")
    implementation("androidx.fragment:fragment-ktx:1.3.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("androidx.activity:activity-ktx:1.3.0-alpha03")
    implementation("androidx.startup:startup-runtime:1.0.0")
    implementation("com.google.android.material:material:$materialVersion")

    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")

    // Paging
    implementation("androidx.paging:paging-runtime-ktx:$pagingVersion")

    // Lifecycle, LiveData
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-reactivestreams-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-common-java8:$lifecycleVersion")

    // Room
    implementation("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    implementation("androidx.room:room-rxjava3:$roomVersion") // optional - RxJava support for Room

    // Work manager
    implementation("androidx.work:work-runtime-ktx:$workVersion") // Kotlin + coroutines

    // Koin
    implementation("org.koin:koin-androidx-viewmodel:$koinVersion")
    implementation("org.koin:koin-androidx-scope:$koinVersion")

    // Moshi, Retrofit, OkHttp
    implementation("com.squareup.moshi:moshi-kotlin:1.11.0")
    implementation("com.squareup.retrofit2:retrofit:$retrofit2Version")
    implementation("com.squareup.retrofit2:converter-moshi:$retrofit2Version")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

    // Leak canary
/*    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.4")*/

    // Stark
    implementation("com.github.anthonystark1977:mvi:4.0.2")

    // RxRelay, RxBinding, Timber
    implementation("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")
    implementation("com.jakewharton.rxrelay3:rxrelay:$rxRelayVersion")
    implementation("com.jakewharton.rxbinding4:rxbinding:$rxBindingVersion")
    implementation("com.jakewharton.rxbinding4:rxbinding-core:$rxBindingVersion")
    implementation("com.jakewharton.rxbinding4:rxbinding-material:$rxBindingVersion")
    implementation("com.jakewharton.rxbinding4:rxbinding-swiperefreshlayout:$rxBindingVersion")
    implementation("com.jakewharton.rxbinding4:rxbinding-recyclerview:$rxBindingVersion")
    implementation("com.jakewharton.timber:timber:$timberVersion")

    // Rx
    implementation("io.reactivex.rxjava3:rxkotlin:3.0.0")
    implementation("io.reactivex.rxjava3:rxjava:3.0.6")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.0")

    // Glide
    implementation("com.github.bumptech.glide:glide:$glideVersion")
    kapt("com.github.bumptech.glide:compiler:$glideVersion")
    implementation("com.github.bumptech.glide:okhttp3-integration:$glideVersion") {
        exclude(group = "glide-parent")
    }

    // Views
    implementation("com.miguelcatalan:materialsearchview:1.4.0")
    implementation("com.ms-square:expandableTextView:0.1.4")
    implementation("com.jaredrummler:material-spinner:1.3.1")
    implementation("com.github.antonKozyriatskyi:CircularProgressIndicator:1.3.0")
    implementation("com.github.chrisbanes:PhotoView:2.3.0")
    implementation("com.chauthai.swipereveallayout:swipe-reveal-layout:1.4.1")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.amulyakhare:com.amulyakhare.textdrawable:1.0.1")
    implementation("com.airbnb.android:lottie:3.4.3")
    implementation("me.relex:circleindicator:2.1.4")
    implementation("com.github.cachapa:ExpandableLayout:2.9.2")

    testImplementation("junit:junit:4.13")
    androidTestImplementation("androidx.test:runner:1.3.0-alpha05")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0-alpha05")

    // Commons-io
    implementation ("commons-io:commons-io:2.6")

    // Jsoup
    implementation("org.jsoup:jsoup:1.13.1")

    // Firebase
    implementation("com.google.firebase:firebase-auth:20.0.1")
    implementation("com.google.firebase:firebase-storage:19.2.1")
    implementation("com.google.firebase:firebase-firestore:22.0.1")
    implementation("com.google.firebase:firebase-analytics:18.0.0")
    implementation("com.google.firebase:firebase-crashlytics:17.3.0")
    implementation("com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava")

}
