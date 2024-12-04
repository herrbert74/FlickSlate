package com.zsoltbertalan.flickslate.convention

import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.internal.dsl.InternalLibraryExtension
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

fun org.gradle.api.Project.commonConfiguration(extension: InternalLibraryExtension) = extension.apply {
	compileSdk = versionCatalog.findVersion("compileSdkVersion").get().requiredVersion.toInt()

	defaultConfig {
		minSdk = versionCatalog.findVersion("minSdkVersion").get().requiredVersion.toInt()
		consumerProguardFiles("consumer-rules.pro")
	}
	//That's not a good idea, but let's keep it for now
//	buildFeatures {
//		buildConfig = true
//	}
}

fun composeConfiguration(extension: CommonExtension<*,*,*,*,*,*>) = extension.apply {
	buildFeatures {
		compose = true
	}
}

fun org.gradle.api.Project.configureKotlin(
	kotlinJvmProjectExtension: KotlinJvmProjectExtension
) {
	kotlinJvmProjectExtension.apply {
		jvmToolchain(versionCatalog.findVersion("jdk").get().requiredVersion.toInt())
	}
}

fun org.gradle.api.Project.configureKotlinAndroid(
	kotlinAndroidProjectExtension: KotlinAndroidProjectExtension
) {
	kotlinAndroidProjectExtension.apply {
		jvmToolchain(versionCatalog.findVersion("jdk").get().requiredVersion.toInt())
	}
}

val org.gradle.api.Project.versionCatalog: VersionCatalog
	get() = extensions.getByType(VersionCatalogsExtension::class.java)
		.named("libs")
