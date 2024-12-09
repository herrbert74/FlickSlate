plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.jetbrains.kotlin.android)
	alias(libs.plugins.google.dagger.hilt.android)
	alias(libs.plugins.ksp)
	alias(libs.plugins.serialization)
	id("android-library-convention")
	id("data-convention")
}

android {
	namespace = "com.zsoltbertalan.flickslate.search.data"

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}
}

dependencies {
	api(project(":search:search-domain"))
	testImplementation(testFixtures(project("::shared")))
	testImplementation(libs.kotlinx.serialization.json)
}
