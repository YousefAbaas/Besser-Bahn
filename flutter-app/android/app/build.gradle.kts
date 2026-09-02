import java.util.Properties

plugins {
    id("com.android.application")
    id("kotlin-android")
    // The Flutter Gradle Plugin must be applied after the Android and Kotlin Gradle plugins.
    id("dev.flutter.flutter-gradle-plugin")
}

android {
    namespace = "dev.chuk.betterbahn"
    compileSdk = flutter.compileSdkVersion
    ndkVersion = flutter.ndkVersion

    compileOptions {
        // Required by flutter_local_notifications (Java 8+ APIs on older devices).
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    val keystoreProperties = Properties()
    val keystoreFile = file("../key.properties")

    if (keystoreFile.exists()) {
        keystoreFile.inputStream().use { input ->
            keystoreProperties.load(input)
        }
    } else {
        println("WARNING: key.properties file not found at ${keystoreFile.absolutePath}")
    }

    signingConfigs {
        create("release") {
            if (keystoreFile.exists()) {
                keyAlias = keystoreProperties["keyAlias"] as String?
                keyPassword = keystoreProperties["keyPassword"] as String?
                storeFile = file("../${keystoreProperties.getProperty("storeFile")}")
                storePassword = keystoreProperties["storePassword"] as String?
            }
        }
    }

    defaultConfig {
        // TODO: Specify your own unique Application ID.
        applicationId = "dev.chuk.betterbahn"

        minSdk = flutter.minSdkVersion
        targetSdk = flutter.targetSdkVersion

        // Monotonic versionCode derived from the semantic version name.
        val plain = flutter.versionName.substringBefore("+").substringBefore("-")
        val semver = plain.split(".")

        val base =
            (semver.getOrNull(0)?.toIntOrNull() ?: 0) * 10000 +
                    (semver.getOrNull(1)?.toIntOrNull() ?: 0) * 100 +
                    (semver.getOrNull(2)?.toIntOrNull() ?: 0)

        val rc = Regex("-rc\\.?(\\d+)")
            .find(flutter.versionName.substringBefore("+"))
            ?.groupValues
            ?.get(1)
            ?.toIntOrNull()

        require(rc == null || rc in 1..9) {
            "rc number must be 1..9, got $rc"
        }

        versionCode = if (rc != null) base - 10 + rc else base
        versionName = flutter.versionName
    }

    buildTypes {
        release {
            signingConfig = if (keystoreFile.exists()) {
                signingConfigs.getByName("release")
            } else {
                signingConfigs.getByName("debug")
            }

            isShrinkResources = true
            isMinifyEnabled = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    dependenciesInfo {
        // Disables dependency metadata when building APKs (for IzzyOnDroid/F-Droid)
        includeInApk = false

        // Disables dependency metadata when building Android App Bundles (for Google Play)
        includeInBundle = false
    }

    packaging {
        // Compress dex + native libs.
        dex.useLegacyPackaging = true
        jniLibs.useLegacyPackaging = true
    }
}

flutter {
    source = "../.."
}

dependencies {
    implementation("androidx.browser:browser:1.9.0")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.4")
}