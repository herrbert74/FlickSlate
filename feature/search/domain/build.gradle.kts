plugins {
	id("android-library-convention")
	id("metro-convention")
	alias(libs.plugins.kotlin.serialization)
}

android {
	namespace = "com.zsoltbertalan.flickslate.search.domain"
}

dependencies {
	api(project(":base:kotlin"))
	api(project(":shared:domain"))
	implementation(libs.kotlinResult.result)
	implementation(libs.kotlinx.coroutinesCore)

}
