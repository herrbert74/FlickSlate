plugins {
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.kotlin.serialization)
	id("android-library-convention")
	id("metro-convention")
}

android {
	namespace = "com.zsoltbertalan.flickslate.base.android"

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}
}

dependencies {
	api(project(":shared:domain"))
	implementation(project(":base:kotlin"))
	implementation(platform(libs.androidx.compose.bom))

	api(libs.kotlinx.coroutinesCore)

}
