plugins {
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.kotlin.composeCompiler)
	alias(libs.plugins.compose.screenshotTesting)
	alias(libs.plugins.kotlin.serialization)
	id("kotlin-parcelize")
	id("android-library-convention")
}

android {
	namespace = "com.zsoltbertalan.flickslate.shared.ui"

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}

	@Suppress("UnstableApiUsage")
	testFixtures.enable = true

	@Suppress("UnstableApiUsage")
	experimentalProperties["android.experimental.enableScreenshotTest"] = true
}

dependencies {
	api(project(":shared:domain"))
	implementation(platform(libs.androidx.compose.bom))

	api(libs.androidx.composeFoundation)
	api(libs.androidx.composeFoundationLayout)
	api(libs.androidx.composeRuntime)
	api(libs.kotlinx.collectionsImmutableJvm)
	api(libs.kotlinx.coroutinesCore)

	implementation(libs.androidx.annotation)
	implementation(libs.androidx.composeMaterial3)
	implementation(libs.androidx.composeMaterialIconsCore)
	implementation(libs.androidx.composeRuntimeAnnotation)
	implementation(libs.androidx.composeUi)
	implementation(libs.androidx.composeUiGeometry)
	implementation(libs.androidx.composeUiGraphics)
	implementation(libs.androidx.composeUiText)
	implementation(libs.androidx.composeUiUnit)
	implementation(libs.androidx.composeUiTooling)
	implementation(libs.androidx.composeUiToolingPreview)
	implementation(libs.androidx.paletteKtx)
	implementation(libs.coil.base)
	implementation(libs.coil.compose)
	implementation(libs.coil.compose.base)
	implementation(libs.timber)

	testImplementation(libs.jUnit)
	testImplementation(libs.kotest.assertionsCore)

	testFixturesImplementation(platform(libs.androidx.compose.bom))

	testFixturesImplementation(libs.androidx.activity)
	testFixturesImplementation(libs.androidx.composeMaterial3)
	testFixturesImplementation(libs.androidx.composeUiTest)
	testFixturesImplementation(libs.androidx.testExtJUnit)
	testFixturesImplementation(libs.androidx.composeUiTestJunit4Android)

	screenshotTestImplementation(libs.androidx.composeUiTooling)
	screenshotTestImplementation(libs.android.screenshotValidationApi)

}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	compilerOptions.freeCompilerArgs.add("-opt-in=androidx.compose.material3.ExperimentalMaterial3Api")
}
