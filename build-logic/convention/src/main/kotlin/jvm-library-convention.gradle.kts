plugins {
	alias(libs.plugins.jetbrains.kotlin.jvm)
}

kotlin {
	jvmToolchain(libs.versions.jdk.get().toInt())
}
