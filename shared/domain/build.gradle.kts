plugins {
	id("android-library-convention")
	alias(libs.plugins.ksp)
	alias(libs.plugins.kotlin.serialization)
	id("kotlin-parcelize")
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
	testFixtures.enable = true

}

dependencies {
	api(libs.kotlinx.serializationCore)

	implementation(libs.kotlin.parcelizeRuntime)
	implementation(libs.timber)
}
