package com.zsoltbertalan.flickslate.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

fun org.gradle.api.Project.commonConfiguration(extension: CommonExtension<*, *, *, *, *, *>) = extension.apply {
	compileSdk = versionCatalog.findVersion("compileSdkVersion").get().requiredVersion.toInt()

	defaultConfig {
		minSdk = versionCatalog.findVersion("minSdkVersion").get().requiredVersion.toInt()
		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
	}
	buildFeatures {
		buildConfig = true
	}
}

fun org.gradle.api.Project.configureKotlinAndroid(
	kotlinAndroidProjectExtension: KotlinAndroidProjectExtension
) {
	kotlinAndroidProjectExtension.apply {
		jvmToolchain(17)
	}
}

val org.gradle.api.Project.versionCatalog
	get() = extensions.getByType(VersionCatalogsExtension::class.java)
		.named("libs")
