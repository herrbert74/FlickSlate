plugins {
	`kotlin-dsl`
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.kotlin.serialization)
}

group = "com.zsoltbertalan.flickslate.buildlogic.profiler"

repositories {
	mavenCentral()
	google()
}

kotlin {
	jvmToolchain(21)
}

dependencies {
	implementation("com.android.tools.build:gradle:9.0.0")

	implementation(libs.kotlinx.serializationCore)
	implementation(libs.kotlinx.serializationJson)
}

gradlePlugin {
	plugins {
		create("profiler") {
			id = "profiler"
			implementationClass = "com.zsoltbertalan.profiler.ProfilerPlugin"
		}
	}
}
