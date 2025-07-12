plugins {
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.detekt)
	alias(libs.plugins.dependencyAnalysis)
	alias(libs.plugins.kotlin.android)
}

apply(from = project.rootProject.file("config/detekt/detekt.gradle"))

android {
	compileSdk = libs.versions.compileSdkVersion.get().toInt()
	defaultConfig {
		minSdk = libs.versions.minSdkVersion.get().toInt()
		consumerProguardFiles("consumer-rules.pro")
	}
}

kotlin {
	jvmToolchain(libs.versions.jdk.get().toInt())
}

dependencies {
	"detektPlugins"(libs.detekt.compose)
}
