@file:Suppress("UnstableApiUsage")

dependencyResolutionManagement {
	repositories {
		google()
		gradlePluginPortal()
		mavenCentral()
	}
}

plugins {
	id("dev.panuszewski.typesafe-conventions") version "0.9.0"
}

rootProject.name = "build-logic"
include(":convention")
include(":loccounter")
