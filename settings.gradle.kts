@file:Suppress("UnstableApiUsage")

pluginManagement {
	includeBuild("build-logic")
	repositories {
		gradlePluginPortal()
		mavenCentral()
		google()
		// Only required for realm-kotlin snapshots
		//maven("https://oss.sonatype.org/content/repositories/snapshots")
	}
}

plugins {
	id("org.gradle.toolchains.foojay-resolver-convention") version("0.5.0")
}

dependencyResolutionManagement {
	repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
	repositories {
		mavenCentral()
		google()
		maven("https://jitpack.io")
		// Only required for realm-kotlin snapshots
		//maven("https://oss.sonatype.org/content/repositories/snapshots")
	}
}

rootProject.name = "FlickSlate"
include(":app")
include(":build-source:convention")
include(":movies:movies-data")
include(":movies:movies-domain")
include(":shared")
include(":shared-data")
