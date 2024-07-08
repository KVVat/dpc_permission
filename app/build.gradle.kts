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
            signingConfig = signingConfigs.getByName("platform")
        }
        create("noperm"){
            dimension = "settings"
            signingConfig = signingConfigs.getByName("platform")
        }

    }
    //flavorDimensions "settings"
    /*
    productFlavors {
        normal {
            dimension "settings"
            signingConfig signingConfigs.norm
        }
        noperm {
            dimension "settings"
            signingConfig signingConfigs.norm
        }
        platform {
            dimension "settings"
            signingConfig signingConfigs.platform
        }
    }*/

    namespace = "com.android.certification.niap.permission.dpctester"
    buildFeatures {
        aidl = true
    }
    compileSdkPreview = "VanillaIceCream"

    defaultConfig {
        applicationId = "com.android.certification.niap.permission.dpctester"
        minSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
    sourceSets {
        getByName("main") {
            aidl {
                srcDirs("src/main/aidl")
            }
        }
    }
}

dependencies {
    // Java language implementation
    implementation(libs.androidx.preference)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.guava)
    implementation(libs.material)

    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}