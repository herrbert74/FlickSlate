@file:Suppress("UnstableApiUsage")

pluginManagement {
	repositories {
		gradlePluginPortal()
		mavenCentral()
		google()
		// Only required for realm-kotlin snapshots
		maven("https://oss.sonatype.org/content/repositories/snapshots")
	}
}
dependencyResolutionManagement {
	repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
	repositories {
		mavenCentral()
		google()
		maven("https://jitpack.io")
		// Only required for realm-kotlin snapshots
		maven("https://oss.sonatype.org/content/repositories/snapshots")
	}
}

rootProject.name = "FlickSlate"
include(":app")
 