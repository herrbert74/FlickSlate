plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.compose.compiler)
	alias(libs.plugins.jetbrains.kotlin.android)
	alias(libs.plugins.ksp)
	id("android-library-convention")
	id("ui-convention")
}

android {
	namespace = "com.zsoltbertalan.flickslate.search.ui"

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}
}

dependencies {
	api(project(":search:search-domain"))
}
