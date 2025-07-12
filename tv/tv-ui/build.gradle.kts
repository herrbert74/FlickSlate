plugins {
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.kotlin.composeCompiler)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.ksp)
	id("android-library-convention")
	id("ui-convention")
}

android {
	namespace = "com.zsoltbertalan.flickslate.tv.ui"

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}
}

dependencies {
	api(project(":tv:tv-domain"))
	implementation(libs.androidx.hiltNavigationCompose)
	api(libs.androidx.lifecycleCommon)
	implementation(libs.androidx.lifecycleRuntimeCompose)
	api(libs.androidx.lifecycleViewmodelCompose)
}
