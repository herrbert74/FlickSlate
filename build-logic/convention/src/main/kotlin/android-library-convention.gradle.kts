import com.android.build.api.dsl.LibraryExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.dsl.abi.AbiValidationExtension
import org.jetbrains.kotlin.gradle.dsl.abi.ExperimentalAbiValidation

plugins {
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.dependencyAnalysis)
}

extensions.configure<LibraryExtension>("android") {
	compileSdk = libs.versions.compileSdkVersion.get().toInt()
	defaultConfig {
		minSdk = libs.versions.minSdkVersion.get().toInt()
		val consumerRules = project.file("consumer-rules.pro")
		if (consumerRules.exists()) {
			consumerProguardFiles(consumerRules)
		}
	}
}

extensions.configure<KotlinProjectExtension>("kotlin") {
	jvmToolchain(libs.versions.jdk.get().toInt())
}

@OptIn(ExperimentalAbiValidation::class)
extensions.findByType<AbiValidationExtension>()?.apply {
	this.enabled.set(true)
}
