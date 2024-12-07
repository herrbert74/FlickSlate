plugins {
	id("android-library-convention")
	alias(libs.plugins.serialization)
}

android {
	namespace = "com.zsoltbertalan.flickslate.search.domain"
}

dependencies {

	api(project(":shared"))

	implementation(libs.kotlinResult.result)
	implementation(libs.kotlinx.collections.immutable.jvm)
	implementation(libs.kotlinx.coroutines.core)

}