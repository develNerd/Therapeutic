import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    kotlin("android")
    id("com.google.gms.google-services")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false
}

val key: String = gradleLocalProperties(rootDir).getProperty("WEB_CLIENT_API_KEY")
// TODO(Store Access Token on Web server and create a function to refresh and share with client)
// TODO(To prevent token from being revoked referesh on server every week)
val squareDefAccessToken: String = gradleLocalProperties(rootDir).getProperty("SQUARE_ACCESS_TOKEN")
val squareDefRefreshToken: String = gradleLocalProperties(rootDir).getProperty("SQUARE_REFRESH_TOKEN")


android {
    project.ext.set("compose_version", "1.1.1")
    compileSdk = 32
    defaultConfig {
        applicationId = "com.flepper.therapeutic.android"
        minSdk = 21
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        viewBinding = true
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    lint {
        checkReleaseBuilds = true
        abortOnError = false
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        compose = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
        getByName("debug") {
            buildConfigField("String", "WEB_CLIENT_API_KEY", key)
            buildConfigField("String", "SQUARE_ACCESS_TOKEN", squareDefAccessToken)
            buildConfigField("String", "SQUARE_REFRESH_TOKEN", squareDefRefreshToken)
        }
    }

    composeOptions {
        kotlinCompilerExtensionVersion = project.ext.get("compose_version") as String
    }
}

dependencies {
    implementation(project(":shared"))
    implementation(platform("com.google.firebase:firebase-bom:30.3.1"))
    implementation("com.google.android.material:material:1.6.1")
    implementation("androidx.appcompat:appcompat:1.4.2")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation("androidx.activity:activity-ktx:1.5.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.0-alpha01")
    implementation("androidx.appcompat:appcompat:1.4.2")

    val nav_version = "2.5.0"
    val compose_version = "1.2.1"

    implementation("com.google.firebase:firebase-analytics")
    // Kotlin
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")
    implementation("androidx.navigation:navigation-compose:$nav_version")
// Java language implementation
    implementation("androidx.navigation:navigation-fragment:$nav_version")
    implementation("androidx.navigation:navigation-ui:$nav_version")

    // Integration with activities
    implementation("androidx.activity:activity-compose:1.5.0")

    // Compose Material Design
    implementation("androidx.compose.material:material:1.1.1")
    // Animations
    implementation("androidx.compose.animation:animation:1.1.1")
    // Tooling support (Previews, etc.)
    debugImplementation("androidx.compose.ui:ui-tooling:1.1.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.1.1")    // Integration with ViewModels
    implementation("androidx.compose.ui:ui-viewbinding:1.1.1")    // Integration with ViewModels
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.0")

    // Testing Navigation
    androidTestImplementation("androidx.navigation:navigation-testing:$nav_version")

    val media3_version = "1.0.0-beta01"

    // For media playback using ExoPlayer
    implementation("androidx.media3:media3-exoplayer:$media3_version")
    // Common functionality for media database components
    implementation("androidx.media3:media3-database:$media3_version")
    // Common functionality for media decoders
    implementation("androidx.media3:media3-decoder:$media3_version")
    // For scheduling background operations using Jetpack Work's WorkManager with ExoPlayer
    implementation("androidx.media3:media3-exoplayer-workmanager:$media3_version")

    implementation("androidx.media3:media3-exoplayer-dash:$media3_version")

    // For loading data using the OkHttp network stack
    implementation("androidx.media3:media3-datasource-okhttp:$media3_version")
    // For integrating with Cast
    implementation("androidx.media3:media3-cast:$media3_version")
    // For building media playback UIs
    implementation("androidx.media3:media3-ui:$media3_version")

    implementation("com.google.android.gms:play-services-cast-framework:21.0.1")
    // Koin main features for Android

    with(Deps.Koin) {
        implementation(core)
        implementation(android)
    }

    // Accompanist
    implementation("com.google.accompanist:accompanist-pager:0.23.0") // Pager
    implementation("com.google.accompanist:accompanist-pager-indicators:0.23.0")// Pager Indicators

    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.0-beta02")

    //Coil
    implementation("io.coil-kt:coil-compose:2.1.0")
    api("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
    api("io.ktor:ktor-client-logging:1.6.6")
    implementation("com.google.accompanist:accompanist-flowlayout:0.26.0-alpha")

    //Glide
    implementation("com.github.bumptech.glide:glide:4.13.2")
    annotationProcessor("com.github.bumptech.glide:compiler:4.13.2")

    implementation("androidx.compose.ui:ui-util:1.1.1")


    //Auth
    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:30.3.1"))

    // Declare the dependency for the Firebase Authentication library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-auth-ktx")

    // Also declare the dependency for the Google Play services library and specify its version
    implementation("com.google.android.gms:play-services-auth:20.2.0")

    //
    implementation("com.google.android.material:material:1.6.1")

    //
    implementation("com.github.prolificinteractive:material-calendarview:2.0.1")



}

