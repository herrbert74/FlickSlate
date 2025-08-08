plugins {
	id("android-library-convention")
	alias(libs.plugins.kotlin.serialization)
}

android {
	namespace = "com.zsoltbertalan.flickslate.account.domain"
	buildFeatures {
		@Suppress("UnstableApiUsage")
		testFixtures {
			enable = true
		}
	}
}

dependencies {

	api(project(":shared"))

	implementation(libs.kotlinResult.result)

	testFixturesApi(project(":shared"))

	testFixturesCompileOnly(libs.kotlinx.collectionsImmutableJvm)

}
