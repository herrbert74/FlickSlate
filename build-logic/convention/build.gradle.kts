plugins {
	`kotlin-dsl`
}

group = "com.zsoltbertalan.flickslate.buildlogic.convention"

kotlin {
	jvmToolchain(21)
}

dependencies {
	implementation(libs.metro.gradlePlugin)
}
