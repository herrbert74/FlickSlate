plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.detekt)
	alias(libs.plugins.jetbrains.kotlin.android)
	alias(libs.plugins.google.dagger.hilt.android)
	alias(libs.plugins.ksp)
	alias(libs.plugins.serialization)
	id("android-library-convention")
	id("data-convention")
}

apply(from = project.rootProject.file("config/detekt/detekt.gradle"))

android {
	namespace = "com.zsoltbertalan.flickslate.movies.data"

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}
}

dependencies {
	implementation(project(":movies:movies-domain"))
	implementation(project(":shared"))
	implementation(project(":shared-data"))
}
