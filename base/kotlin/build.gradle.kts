plugins {
	id("jvm-library-convention")
	alias(libs.plugins.kotlin.serialization)
}

dependencies {
	api(libs.inject)
	api(libs.kotlinx.coroutinesCore)
	implementation(libs.kotlinResult.result)
}
