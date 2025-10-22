plugins {
	id("android-library-convention")
	alias(libs.plugins.kotlin.serialization)
}

android {
	namespace = "com.zsoltbertalan.flickslate.account.domain"
	buildFeatures {
		@Suppress("UnstableApiUsage")
		testFixtures.enable = true
	}
}

dependencies {
	api(project(":base:kotlin"))
	api(project(":shared:domain"))
	api(libs.inject)

	implementation(libs.kotlinResult.result)
	implementation(libs.kotlinResult.coroutines)
	implementation(libs.kotlinx.coroutinesCore)
	testFixturesApi(project(":shared:domain"))

	testFixturesCompileOnly(libs.kotlinx.collectionsImmutableJvm)

}
