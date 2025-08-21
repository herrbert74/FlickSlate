plugins {
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.dagger.hiltAndroid)
	alias(libs.plugins.ksp)
	alias(libs.plugins.kotlin.serialization)
	id("android-library-convention")
	id("dagger-convention")
	id("data-convention")
}

android {
	namespace = "com.zsoltbertalan.flickslate.tv.data"

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}
}

dependencies {
	api(project(":feature:tv:domain"))
	implementation(libs.baBeStudios.baseData)
	testImplementation(libs.kotest.assertionsCore)
	testImplementation(libs.mockk.dsl)
	testImplementation(libs.okhttp3.mockWebServer)
	testImplementation(libs.retrofit.converterKotlinxSerialization)
	testRuntimeOnly(libs.robolectric)
	testImplementation(testFixtures(project("::feature:tv:domain")))
	testImplementation(testFixtures(project("::shared:data")))

}
