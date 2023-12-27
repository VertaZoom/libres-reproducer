import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.cocoapods)
    alias(libs.plugins.android.application)
    alias(libs.plugins.libres)
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    applyDefaultHierarchyTemplate()

    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = JavaVersion.VERSION_17.majorVersion
            }
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        version = "1.0.0"
        summary = "app"
        homepage = "empty"
        ios.deploymentTarget = "12.0"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "app"
            isStatic = true
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":library"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val androidMain by getting {
            dependsOn(commonMain)
            dependencies {
            }
        }

        val iosMain by getting {
            dependsOn(commonMain)
            dependencies {
            }
        }
    }
}

android {
    namespace = "com.example.reproducer"
    compileSdk = 34
    defaultConfig {
        minSdk = 21
        targetSdk = 34

        applicationId = "com.example.reproducer"
        versionCode = 1
        versionName = "1.0.0"
    }
    sourceSets["main"].apply {
        manifest.srcFile("src/androidMain/AndroidManifest.xml")
        res.srcDirs("src/androidMain/resources", "src/commonMain/resources")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    packaging {
        resources.excludes.add("META-INF/**")
    }
}

tasks.register("printDeps", org.gradle.api.tasks.diagnostics.DependencyReportTask::class) {
    configurations = project.buildscript.configurations + project.configurations
}

tasks.register<Copy>("copyLibresBundles") {
    from("../library/build/generated/libres/apple") {
        include("libres-bundles/**")
    }
    into("$buildDir/generated/libres/apple")
    includeEmptyDirs = true
}

tasks.register("updatePodspecFile") {
    val filePath = "$projectDir/app.podspec"
    val fileContent = File(filePath).readText()
    val updatedContent = fileContent.replace(
        "spec.resources = ['build/generated/libres/apple/libres-bundles']",
        "spec.resources = ['../library/build/generated/libres/apple/libres-bundles']"
    )
    File(filePath).writeText(updatedContent)
}

tasks.withType<DependencyReportTask>().configureEach {
    this.taskConfigurations.forEach {
        println("configurations: ${it.name}")
    }
    println("DependencyReportTask: $name")
    if (name == "libresSetupPodspecExports") {
        finalizedBy("copyLibresBundles")
//        finalizedBy("updatePodspecFile")
    }
}

//tasks.named("libresSetupPodspecExports") {
//    finalizedBy("copyLibresBundles")
//}