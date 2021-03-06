// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    val kotlinVersion by extra("1.4.21")
    val navVersion by extra("2.3.0")

    extra.run {
        set("coroutinesVersion", "1.3.9")
        set("lifecycleVersion", "2.3.0-alpha07")
        set("pagingVersion", "2.1.2")
        set("koinVersion", "2.2.0")
        set("materialVersion", "1.2.0")
        set("glideVersion", "4.11.0")
        set("rxBindingVersion", "4.0.0")
        set("timberVersion", "4.7.1")
        set("rxRelayVersion", "3.0.0")
        set("retrofit2Version", "2.9.0")
        set("roomVersion", "2.3.0-alpha02")
        set("workVersion", "2.5.0-alpha01")
    }

    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.0.1")
        classpath(kotlin("gradle-plugin", kotlinVersion))
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navVersion")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}

tasks.register("clean", Delete::class) { delete(rootProject.buildDir) }

