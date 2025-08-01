plugins {
	id("android-library-convention")
	alias(libs.plugins.kotlin.serialization)
}

android {
	namespace = "com.zsoltbertalan.flickslate.movies.domain"
	buildFeatures {
		@Suppress("UnstableApiUsage")
		testFixtures {
			enable = true
		}
	}
}

dependencies {

	api(project(":shared"))

	api(libs.jakarta.inject) // transitive
	api(libs.kotlinx.collectionsImmutableJvm)

	implementation(libs.kotlinResult.result)
	implementation(libs.kotlinx.coroutinesCore)

	testFixturesApi(project(":shared"))

	testFixturesCompileOnly(libs.kotlinx.collectionsImmutableJvm)

}
