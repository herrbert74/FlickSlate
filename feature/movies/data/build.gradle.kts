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
	implementation(project(":feature:movies:domain"))

	testImplementation(libs.kotest.assertionsCore)
	testImplementation(libs.mockk.dsl)
	testImplementation(libs.okhttp3.mockWebServer)
	testImplementation(libs.retrofit.converterKotlinxSerialization)
	testImplementation(testFixtures(project(":feature:movies:data")))
	testImplementation(testFixtures(project(":feature:movies:domain")))
	testImplementation(testFixtures(project(":shared:data")))
	testImplementation(testFixtures(project(":shared:domain")))
	testRuntimeOnly(libs.robolectric)

	testFixturesApi(project(":base:kotlin"))
	testFixturesApi(project(":feature:movies:domain"))
	testFixturesApi(project(":shared:domain"))
	testFixturesApi(libs.inject)

	testFixturesImplementation(project(":shared:data"))
	testFixturesImplementation(testFixtures(project(":feature:movies:domain")))
	testFixturesImplementation(testFixtures(project(":shared:domain")))
	testFixturesImplementation(libs.kotlinResult.result)
	testFixturesImplementation(libs.autobind.android.testing)
	testFixturesImplementation(libs.dagger.core)
	testFixturesImplementation(libs.dagger.hiltAndroid)
	testFixturesImplementation(libs.dagger.hiltCore)

	kspTestFixtures(libs.autobind.compiler)
	kspTestFixtures(libs.dagger.compiler)
	kspTestFixtures(libs.dagger.hiltCompiler)
}
