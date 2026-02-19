plugins {
	id("android-library-convention")
	id("metro-convention")
	alias(libs.plugins.kotlin.serialization)
}

android {
	namespace = "com.zsoltbertalan.flickslate.movies.domain"

	buildFeatures {
		@Suppress("UnstableApiUsage")
		testFixtures.enable = true
	}
}

dependencies {
	api(project(":base:kotlin"))
	api(project(":shared:domain"))
	api(project(":feature:account:domain"))
	api(libs.kotlinx.collectionsImmutableJvm)

	implementation(libs.kotlinResult.result)
	implementation(libs.kotlinResult.coroutines)
	implementation(libs.kotlinx.coroutinesCore)
	testFixturesApi(libs.kotlin.stdlib)
	testFixturesImplementation(project(":shared:domain"))

	testFixturesCompileOnly(libs.kotlinx.collectionsImmutableJvm)

	testImplementation(libs.jUnit)
	testImplementation(libs.kotest.assertionsCore)
	testImplementation(libs.mockk.library)
	testImplementation(libs.mockk.core)
	testImplementation(libs.mockk.dsl)
}
