plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.detekt)
	alias(libs.plugins.jetbrains.kotlin.android)
	alias(libs.plugins.google.dagger.hilt.android)
	alias(libs.plugins.ksp)
	alias(libs.plugins.serialization)
	id("android-library-convention")
	id("data-convention")
}

apply(from = project.rootProject.file("config/detekt/detekt.gradle"))

android {
	namespace = "com.zsoltbertalan.flickslate.movies.data"

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}
	@Suppress("UnstableApiUsage")
	testFixtures {
		enable = true
	}
}

dependencies {
	api(project(":movies:movies-domain"))
	testImplementation(testFixtures(project("::movies:movies-data")))
	testImplementation(testFixtures(project("::movies:movies-domain")))

	//Remove in AGP 8.9.0 https://issuetracker.google.com/issues/340315591
	testFixturesCompileOnly("org.jetbrains.kotlin:kotlin-stdlib:2.1.0")
}
