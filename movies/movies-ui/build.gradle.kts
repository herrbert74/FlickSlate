plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.compose.compiler)
	alias(libs.plugins.compose.screenshot.testing)
	alias(libs.plugins.jetbrains.kotlin.android)
	alias(libs.plugins.ksp)
	id("android-library-convention")
	id("ui-convention")
}

android {
	namespace = "com.zsoltbertalan.flickslate.movies.ui"

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}

	experimentalProperties["android.experimental.enableScreenshotTest"] = true
}

dependencies {
	api(project(":movies:movies-domain"))
	implementation(libs.androidx.hilt.navigation.compose)
	api(libs.androidx.lifecycle.common)
	implementation(libs.androidx.lifecycle.runtime.compose)
	api(libs.androidx.lifecycle.viewmodel.compose)

	testImplementation(testFixtures(project("::movies:movies-domain")))

	screenshotTestImplementation(libs.androidx.compose.ui.tooling)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	compilerOptions.freeCompilerArgs.add("-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi")
}
