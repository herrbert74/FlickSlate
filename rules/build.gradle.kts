plugins {
	alias(libs.plugins.kotlin.jvm)
}

tasks.withType<Test>().configureEach {
	useJUnitPlatform()
}

dependencies {
	compileOnly(libs.detekt.api)
	testImplementation(libs.detekt.test)
	testImplementation(libs.detekt.testUtils)
	testImplementation(libs.kotest.assertionsCore)
	testImplementation(libs.jUnit)
	testImplementation(libs.assertj)
	testImplementation(libs.jUnit5.jupiterApi)
	testImplementation(libs.jUnit5.jupiterEngine)
	testRuntimeOnly(libs.jUnit5.jupiterEngine)
	testRuntimeOnly(libs.jUnit5.platformLauncher)
}
