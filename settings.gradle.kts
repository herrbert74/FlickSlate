@file:Suppress("UnstableApiUsage")

includeBuild("build-logic")

pluginManagement {
	repositories {
		gradlePluginPortal()
		mavenCentral()
		google()
		// Only required for realm-kotlin snapshots
		// maven("https://oss.sonatype.org/content/repositories/snapshots")
	}
}

plugins {
	id("org.gradle.toolchains.foojay-resolver-convention") version ("1.0.0")
}

dependencyResolutionManagement {
	repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
	repositories {
		mavenCentral()
		google()
		maven("https://jitpack.io")
		// Only required for realm-kotlin snapshots
		// maven("https://oss.sonatype.org/content/repositories/snapshots")
	}
}

rootProject.name = "FlickSlate"
include(":app")

include(":base:android")
include(":base:kotlin")
include(":feature:account:data")
include(":feature:account:domain")
include(":feature:account:ui")
include(":feature:movies:data")
include(":feature:movies:domain")
include(":feature:movies:ui")
include(":feature:search:data")
include(":feature:search:domain")
include(":feature:search:ui")
include(":feature:tv:data")
include(":feature:tv:domain")
include(":feature:tv:ui")
include(":shared:domain")
include(":shared:data")
include(":shared:ui")
