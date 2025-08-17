plugins {
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.ksp)
	alias(libs.plugins.kotlin.serialization)
	id("kotlin-parcelize")
	id("android-library-convention")
}

android {
	namespace = "com.zsoltbertalan.flickslate.shared.domain"

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}

	@Suppress("UnstableApiUsage")
	testFixtures {
		enable = true
	}

}

dependencies {
	api(libs.kotlinx.serializationCore)

	implementation(libs.kotlin.parcelizeRuntime)
	implementation(libs.timber)
}
