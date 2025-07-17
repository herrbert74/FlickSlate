plugins {
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.dagger.hiltAndroid)
	alias(libs.plugins.ksp)
	alias(libs.plugins.kotlin.serialization)
	id("android-library-convention")
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
	testFixtures {
		enable = true
	}
}

dependencies {
	api(project(":movies:movies-domain"))
	testImplementation(libs.kotest.assertionsCore)
	testImplementation(libs.kotest.assertionsShared)
	testImplementation(libs.mockk.dsl)
	testImplementation(libs.okhttp3.mockWebServer)
	testImplementation(libs.retrofit.converterKotlinxSerialization)
	testImplementation(libs.robolectric)
	testImplementation(testFixtures(project("::movies:movies-domain")))

	testFixturesApi(project(":shared-data"))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	compilerOptions.freeCompilerArgs.add("-opt-in=okhttp3.ExperimentalOkHttpApi")
}
