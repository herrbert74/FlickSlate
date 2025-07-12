plugins {
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.kotlin.composeCompiler)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.ksp)
	alias(libs.plugins.dagger.hiltAndroid)
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

	implementation(libs.androidx.composeAnimation)
	debugImplementation(libs.androidx.activity)
	implementation(libs.androidx.composeAnimationCore)
	testImplementation(libs.jUnit)
	testImplementation(libs.robolectric)
	testImplementation(libs.androidx.composeUiTestJunit4)
	testImplementation(libs.androidx.hiltNavigationCompose)
	testImplementation(libs.autobind.android.api)
	testImplementation(libs.autobind.android.testing)
	testImplementation(libs.dagger.hiltAndroidTesting)
	testImplementation(libs.dagger.core)
	testImplementation(libs.dagger.hiltCore)
	implementation(libs.dagger.hiltAndroid)
	implementation(libs.inject)
	testImplementation(testFixtures(project("::shared")))
	testImplementation(testFixtures(project("::movies:movies-domain")))
	add("ksp", libs.dagger.hiltAndroidCompiler)
	//Needed for createComposeRule, NOT ONLY for createAndroidComposeRule, as in the docs
	debugRuntimeOnly(libs.androidx.composeUiTestManifest)

	testImplementation(testFixtures(project("::shared")))

	kspTest(libs.autobind.compiler)
	kspTest(libs.androidx.hiltCompiler)
	kspTest(libs.dagger.compiler)
	kspTest(libs.dagger.hiltAndroidCompiler)
}
