plugins {
	`kotlin-dsl`
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.kotlin.serialization)
}

group = "com.zsoltbertalan.flickslate.buildlogic.loccounter"

repositories {
	mavenCentral()
	google()
}

kotlin {
	jvmToolchain(21)
}

dependencies {
	implementation("com.android.tools.build:gradle:8.13.1")

	implementation(libs.kotlinx.serializationCore)
	implementation(libs.kotlinx.serializationJson)
}

gradlePlugin {
	plugins {
		create("linesOfCode") {
			id = "loccounter"
			implementationClass = "com.zsoltbertalan.loccounter.LinesOfCodePlugin"
		}
	}
}
