import org.jetbrains.kotlin.config.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}
val homePath = System.getenv("user.home")
android {
    signingConfigs {
        // TODO :
        // Prepare your own normal.jks and platform.jks as settings and put these into
        // assigned path. It helps to build the application variants automatically.
        // For details see the instructions on the SIGNING.md
        create("norm"){
            storeFile = File(rootProject.projectDir,"./security/normal/normal.jks")
            storePassword = "android"
            keyPassword = "android"
            keyAlias = "normal"
        }
        create("platform") {
            storeFile = File(rootProject.projectDir,"./security/platform/platform.jks")
            storePassword = "android"
            keyPassword = "android"
            keyAlias = "platform"
        }
        create("debugkeystore") {
            storeFile = File(homePath,"./.android/debug.keystore")
            storePassword = "android"
            keyPassword = "androiddebugkey"
            keyAlias = "android"
        }
    }
    val applicationName = "DPCTester"
    val publish = project.tasks.create("publishAll")
    applicationVariants.all {
        val variant = this
        variant.outputs
            .map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }
            .forEach { output ->
                val flavor = variant.productFlavors[0].name
                val outputFileName = "${applicationName}-${flavor}-${variant.buildType.name}.apk"
                println("OutputFileName: $outputFileName")
                output.outputFileName = outputFileName
            }

        if(variant.buildType.name.equals("debug")){
            val task = project.tasks.create("publish${variant.name.capitalize()}Apk", Copy::class)
            mkdir("$rootDir/package")
            variant.outputs.forEach { it->
                task.from(it.outputFile.absolutePath)
            }
            task.into("$rootDir/package")
            task.dependsOn(variant.assembleProvider)
            publish.dependsOn(task)
            // variant.assemble
            //        publish.dependsOn task
        }
    }


    buildTypes {
        release {
            //set null and later set it with productFlavors
            signingConfig = null
            isDebuggable = false
            isMinifyEnabled = false
            //proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
        debug {
            signingConfig = null
            isDebuggable = true
        }
    }
    flavorDimensions.add("settings")
    productFlavors {
        create("normal"){
            dimension = "settings"
            signingConfig = signingConfigs.getByName("norm")
        }
        create("noperm"){
            dimension = "settings"
            signingConfig = signingConfigs.getByName("norm")
        }
        create("platform"){
            dimension = "settings"
            signingConfig = signingConfigs.getByName("platform")
        }
        create("dpc-normal"){
            dimension = "settings"
            signingConfig = signingConfigs.getByName("platform")
        }
        create("dpc-noperm"){
            dimension = "settings"
            signingConfig = signingConfigs.getByName("platform")
        }
    }


    namespace = "com.android.certification.niap.permission.dpctester"
    buildFeatures {
        aidl = true
    }
    compileSdkPreview = "VanillaIceCream"

    defaultConfig {
        applicationId = "com.android.certification.niap.permission.dpctester"
        minSdk = 28
        targetSdk = 35
        //targetSdkPreview = "VanillaIceCream"
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
    sourceSets {
        getByName("main") {
            aidl {
                srcDirs("src/main/aidl")
            }
            assets {
                srcDirs("src/main/assets")
            }
        }
    }
}

dependencies {
    // Java language implementation
    implementation(libs.androidx.preference)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.runner)
    implementation(libs.androidx.credentials)

    implementation(libs.guava)
    implementation(libs.material)

    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.work.runtime)

    implementation(libs.jackson.core)
    implementation(libs.jackson.databind)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}