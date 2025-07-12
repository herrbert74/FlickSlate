plugins {
	`kotlin-dsl`
}

group = "com.zsoltbertalan.flickslate.buildlogic"

kotlin {
	jvmToolchain(21)
}

dependencies {
	//Plugins used as library references here
	implementation(libs.androidGradlePlugin)
	implementation(libs.detektPlugin)
	implementation(libs.dependencyAnalysisPlugin)
	implementation(libs.kotlinJvmPlugin)
	implementation(libs.kotlinAndroidPlugin)
	implementation(libs.kspPlugin)
	implementation(libs.daggerHiltPlugin)
}
