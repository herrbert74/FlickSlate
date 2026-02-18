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

}

dependencies {
	api(project(":feature:search:domain"))

	debugRuntimeOnly(libs.androidx.composeUiTestManifest)

	implementation(libs.androidx.composeAnimation)
	implementation(libs.androidx.composeAnimationCore)

	testImplementation(testFixtures(project(":shared:domain")))
	testImplementation(testFixtures(project(":shared:ui")))
	testImplementation(libs.kotlinx.coroutinesTest)

	testImplementation(libs.androidx.activity)
	testImplementation(libs.androidx.composeUiTest) // transitive
	testImplementation(libs.androidx.composeUiTestJunit4)
	testImplementation(libs.androidx.lifecycleCommon) // transitive
	testImplementation(libs.androidx.lifecycleRuntimeCompose) // transitive
	testImplementation(libs.androidx.lifecycleViewmodelCompose) // transitive
	testImplementation(libs.androidx.testExtJUnit)
	testImplementation(libs.androidx.testCore) // transitive
	testImplementation(libs.jUnit)
	testRuntimeOnly(libs.robolectric)

}
