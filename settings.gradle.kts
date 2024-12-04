@file:Suppress("UnstableApiUsage")

include(":search:search-ui")


include(":search:search-data")


include(":search:search-domain")


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
include(":movies:movies-ui")
include(":shared")
include(":shared-data")
include(":tv:tv-data")
include(":tv:tv-domain")
include(":tv:tv-ui")
