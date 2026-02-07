plugins {
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.ksp)
	alias(libs.plugins.kotlin.serialization)
	id("android-library-convention")
	id("dagger-convention")
	id("data-convention")
	id("metro-convention")
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
	api(project(":feature:search:domain"))
	testImplementation(testFixtures(project(":shared:domain")))

	testImplementation(libs.kotest.assertionsCore)
	testImplementation(libs.kotlinx.serializationJson)
}
