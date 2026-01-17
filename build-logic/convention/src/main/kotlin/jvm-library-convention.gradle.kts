import org.jetbrains.kotlin.gradle.dsl.abi.ExperimentalAbiValidation

plugins {
	alias(libs.plugins.kotlin.jvm)
}

kotlin {
	jvmToolchain(libs.versions.jdk.get().toInt())

	@OptIn(ExperimentalAbiValidation::class)
	abiValidation {
		enabled = true
	}
}
