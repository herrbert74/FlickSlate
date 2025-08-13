plugins {
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.kotlin.composeCompiler)
	alias(libs.plugins.compose.screenshotTesting)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.ksp)
	id("android-library-convention")
	id("coil-convention")
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
	api(project(":feature:movies:domain"))

	api(libs.androidx.lifecycleCommon)
	api(libs.androidx.lifecycleViewmodelCompose)

	implementation(libs.androidx.composeUiToolingPreview) // transitive
	implementation(libs.androidx.coreKtx) // transitive
	implementation(libs.androidx.hiltNavigationCompose)
	implementation(libs.androidx.lifecycleRuntimeCompose)

	testImplementation(testFixtures(project(":feature:movies:domain")))

	testImplementation(libs.kotest.assertionsCore)
	testImplementation(libs.kotest.assertionsShared)
	testImplementation(libs.mockk.dsl) // transitive

	screenshotTestImplementation(libs.androidx.composeUiTooling)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	compilerOptions.freeCompilerArgs.add("-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi")
}
