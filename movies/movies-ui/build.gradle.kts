plugins {
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.kotlin.composeCompiler)
	alias(libs.plugins.compose.screenshotTesting)
	alias(libs.plugins.kotlin.android)
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

	@Suppress("UnstableApiUsage")
	experimentalProperties["android.experimental.enableScreenshotTest"] = true
}

dependencies {
	api(project(":movies:movies-domain"))
	implementation(libs.androidx.hiltNavigationCompose)
	api(libs.androidx.lifecycleCommon)
	implementation(libs.androidx.lifecycleRuntimeCompose)
	api(libs.androidx.lifecycleViewmodelCompose)

	testImplementation(testFixtures(project("::movies:movies-domain")))

	screenshotTestImplementation(libs.androidx.composeUiTooling)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	compilerOptions.freeCompilerArgs.add("-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi")
}
