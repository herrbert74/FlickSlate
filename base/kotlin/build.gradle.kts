plugins {
	id("jvm-library-convention")
	alias(libs.plugins.kotlin.serialization)
	id("metro-convention")
}

dependencies {
	api(libs.kotlinx.coroutinesCore)
	implementation(libs.kotlinResult.result)
}
