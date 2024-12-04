plugins {
	id("android-library-convention")
	alias(libs.plugins.serialization)
}

android {
	namespace = "com.zsoltbertalan.flickslate.movies.domain"
}

dependencies {

	implementation(project(":shared"))

	implementation(libs.kotlinx.serialization.json)
	implementation(libs.kotlinResult.result)
	implementation(libs.kotlinResult.coroutines)
	implementation(libs.kotlinx.coroutines.core)

}