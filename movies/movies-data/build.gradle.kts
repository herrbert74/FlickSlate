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
	testImplementation(libs.squareUp.okhttp3.mockWebServer)
	testImplementation(testFixtures(project("::movies:movies-domain")))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	compilerOptions.freeCompilerArgs.add("-opt-in=okhttp3.ExperimentalOkHttpApi")
}
