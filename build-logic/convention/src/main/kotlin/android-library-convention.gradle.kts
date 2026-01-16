plugins {
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.dependencyAnalysis)
}

// android {
extensions.configure<com.android.build.api.dsl.LibraryExtension>("android") {
	compileSdk = libs.versions.compileSdkVersion.get().toInt()
	defaultConfig {
		minSdk = libs.versions.minSdkVersion.get().toInt()
		consumerProguardFiles("consumer-rules.pro")
	}
}

// kotlin {
extensions.configure<org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension>("kotlin") {
	jvmToolchain(libs.versions.jdk.get().toInt())
}

@OptIn(org.jetbrains.kotlin.gradle.dsl.abi.ExperimentalAbiValidation::class)
// abiValidation {
extensions.findByType<org.jetbrains.kotlin.gradle.dsl.abi.AbiValidationExtension>()?.apply {
	this.enabled.set(true)
}
