plugins {
	alias(libs.plugins.kotlin.jvm)
}

dependencies {
	compileOnly(libs.detekt.api)
	testImplementation(libs.detekt.test)
	testImplementation(libs.kotest.assertionsCore)
	testImplementation(libs.jUnit)
}
