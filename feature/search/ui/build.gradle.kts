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
	api(project(":feature:search:domain"))

	api(libs.inject)

	// Needed for createComposeRule, NOT ONLY for createAndroidComposeRule, as in the docs
	debugRuntimeOnly(libs.androidx.composeUiTestManifest)

	implementation(libs.androidx.composeAnimation)
	implementation(libs.androidx.composeMaterialIconsCore) // transitive
	implementation(libs.androidx.composeAnimationCore)
	implementation(libs.dagger.hiltAndroid)

	ksp(libs.dagger.hiltAndroidCompiler)

	kspTest(libs.autobind.compiler)
	kspTest(libs.androidx.hiltCompiler)
	kspTest(libs.dagger.compiler)
	kspTest(libs.dagger.hiltAndroidCompiler)

	testImplementation(testFixtures(project("::feature:movies:domain")))
	testImplementation(testFixtures(project("::shared:domain")))
	testImplementation(testFixtures(project("::shared:ui")))

	testImplementation(libs.androidx.activity)
	testImplementation(libs.androidx.annotation) // transitive
	testImplementation(libs.androidx.composeUiTest) // transitive
	testImplementation(libs.androidx.composeUiTestJunit4)
	testImplementation(libs.androidx.fragmentKtx) // transitive
	testImplementation(libs.androidx.hiltNavigationCompose)
	testImplementation(libs.androidx.lifecycleCommon) // transitive
	testImplementation(libs.androidx.lifecycleRuntimeCompose) // transitive
	testImplementation(libs.androidx.lifecycleViewmodelCompose) // transitive
	testImplementation(libs.androidx.testExtJUnit)
	testImplementation(libs.androidx.testCore) // transitive
	testImplementation(libs.autobind.android.api)
	testCompileOnly(libs.autobind.android.testing)
	testImplementation(libs.autobind.core) // transitive
	testImplementation(libs.dagger.hiltAndroidTesting)
	testImplementation(libs.dagger.hiltCore)
	testImplementation(libs.guava.jre) // transitive
	testImplementation(libs.jUnit)
	testRuntimeOnly(libs.robolectric)
	testImplementation(libs.robolectric.annotations) // transitive
}
