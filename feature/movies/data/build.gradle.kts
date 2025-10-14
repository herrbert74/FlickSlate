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
	namespace = "com.zsoltbertalan.flickslate.movies.data"

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}

	@Suppress("UnstableApiUsage")
	testFixtures.enable = true
}

dependencies {
	api(project(":feature:movies:domain"))
	api(project(":feature:account:domain"))

	implementation(libs.kotlinx.serializationJson)

	testImplementation(libs.kotest.assertionsCore)
	testImplementation(libs.mockk.dsl)
	testImplementation(libs.okhttp3.mockWebServer)
	testImplementation(libs.retrofit.converterKotlinxSerialization)
	testImplementation(testFixtures(project(":feature:movies:data")))
	testImplementation(testFixtures(project(":shared:data")))
	testImplementation(testFixtures(project(":shared:domain")))
	testRuntimeOnly(libs.robolectric)

	testFixturesImplementation(project(":shared:data"))
}
