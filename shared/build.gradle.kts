plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    kotlin("plugin.serialization") version "1.5.31"
    id("io.realm.kotlin")

}

version = "1.0"

kotlin {
    android()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    val coroutinesVersion = "1.5.2"
    val serializationVersion = "1.3.1"
    val ktorVersion = "1.6.6"

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
        }
    }
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-auth:$ktorVersion")
                implementation("io.ktor:ktor-client-cio:$ktorVersion")
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-serialization:$ktorVersion")
                implementation("io.ktor:ktor-client-logging:$ktorVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
                with(Deps.Koin) {
                    api(core)
                    api(test)
                }
                with(Deps.Firebase) {
                    api(auth)
                    api(fireStore)
                }
                with(Deps.Kermit){
                    implementation(kermitMain)
                }
                with(Deps.RealDatabase){
                    implementation(this.coroutines)
                    implementation(this.realBase)
                }
                implementation(kotlin("stdlib-common"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting{
            dependencies{

                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
            }
        }
        val androidTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)

        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    compileSdk = 32
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 21
        targetSdk = 32
    }
}

