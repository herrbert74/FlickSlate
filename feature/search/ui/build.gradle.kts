plugins {
	id("android-library-convention")
	alias(libs.plugins.kotlin.composeCompiler)
	id("metro-convention")
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
	testOptions.unitTests.isIncludeAndroidResources = true

	sourceSets {
		getByName("test") {
			java.setSrcDirs(emptyList<File>())
			kotlin.setSrcDirs(emptyList<File>())
		}
	}
}

dependencies {
	api(project(":feature:search:domain"))

	debugRuntimeOnly(libs.androidx.composeUiTestManifest)

	implementation(libs.androidx.composeAnimation)
	implementation(libs.androidx.composeAnimationCore)
}
