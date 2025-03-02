plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.compose.compiler)
	alias(libs.plugins.jetbrains.kotlin.android)
	alias(libs.plugins.ksp)
	alias(libs.plugins.google.dagger.hilt.android)
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
	testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
	api(project(":search:search-domain"))

	implementation(libs.androidx.compose.animation)
	debugImplementation(libs.androidx.activity.activity)
	implementation(libs.androidx.compose.animation.core)
	testImplementation(libs.junit)
	testImplementation(libs.test.robolectric)
	testImplementation(libs.androidx.compose.ui.test.junit4)
	testImplementation(libs.androidx.hilt.navigation.compose)
	testImplementation(libs.autobind.android.api)
	testImplementation(libs.autobind.android.testing)
	testImplementation(libs.google.dagger.hilt.androidTesting)
	testImplementation(libs.google.dagger.core)
	testImplementation(libs.google.dagger.hilt.core)
	implementation(libs.google.dagger.hilt.android)
	implementation(libs.inject)
	testImplementation(testFixtures(project("::shared")))
	testImplementation(testFixtures(project("::movies:movies-domain")))
	add("ksp", libs.google.dagger.hilt.androidCompiler)
	//Needed for createComposeRule, NOT ONLY for createAndroidComposeRule, as in the docs
	debugRuntimeOnly(libs.androidx.compose.ui.testManifest)

	testImplementation(testFixtures(project("::shared")))

	kspTest(libs.autobind.compiler)
	kspTest(libs.androidx.hilt.compiler)
	kspTest(libs.google.dagger.compiler)
	kspTest(libs.google.dagger.hilt.androidCompiler)
}
