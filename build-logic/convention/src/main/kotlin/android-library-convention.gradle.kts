plugins {
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.dependencyAnalysis)
	alias(libs.plugins.kotlin.android)
}

android {
	compileSdk = libs.versions.compileSdkVersion.get().toInt()
	defaultConfig {
		minSdk = libs.versions.minSdkVersion.get().toInt()
		consumerProguardFiles("consumer-rules.pro")
	}
}

kotlin {
	jvmToolchain(libs.versions.jdk.get().toInt())

	@OptIn(org.jetbrains.kotlin.gradle.dsl.abi.ExperimentalAbiValidation::class)
	abiValidation {
		enabled = true
	}
}
