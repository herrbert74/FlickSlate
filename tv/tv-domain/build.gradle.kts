plugins {
	id("android-library-convention")
	alias(libs.plugins.serialization)
}

android {
	namespace = "com.zsoltbertalan.flickslate.tv.domain"

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}
}

dependencies {

	implementation(project(":shared"))

	implementation(libs.kotlinx.serialization.json)
	implementation(libs.kotlinResult.result)
	implementation(libs.kotlinResult.coroutines)
	implementation(libs.kotlinx.coroutines.core)

}
