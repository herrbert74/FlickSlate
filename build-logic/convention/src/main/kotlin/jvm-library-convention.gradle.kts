plugins {
	alias(libs.plugins.kotlin.jvm)
}

kotlin {
	jvmToolchain(libs.versions.jdk.get().toInt())
}
