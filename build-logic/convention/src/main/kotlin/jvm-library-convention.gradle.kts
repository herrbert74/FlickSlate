plugins {
	alias(libs.plugins.kotlin.jvm)
}

kotlin {
	jvmToolchain(libs.versions.jdk.get().toInt())

	@OptIn(org.jetbrains.kotlin.gradle.dsl.abi.ExperimentalAbiValidation::class)
	abiValidation {
		enabled = true
	}
}
