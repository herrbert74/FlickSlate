plugins {
	id("android-library-convention")
	alias(libs.plugins.kotlin.serialization)
}

android {
	namespace = "com.zsoltbertalan.flickslate.search.domain"
}

dependencies {

	api(project(":shared:domain"))
	implementation(libs.kotlinResult.result)
	implementation(libs.kotlinx.coroutinesCore)

}
