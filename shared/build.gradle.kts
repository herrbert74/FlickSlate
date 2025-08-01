plugins {
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.kotlin.composeCompiler)
	alias(libs.plugins.compose.screenshotTesting)
	alias(libs.plugins.dagger.hiltAndroid)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.ksp)
	alias(libs.plugins.kotlin.serialization)
	id("kotlin-parcelize")
	id("android-library-convention")
}

android {
	namespace = "com.zsoltbertalan.flickslate.shared"

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

	experimentalProperties["android.experimental.enableScreenshotTest"] = true
}

dependencies {
	implementation(platform(libs.androidx.compose.bom))

	api(libs.androidx.composeFoundation)
	api(libs.androidx.composeFoundationLayout)
	api(libs.androidx.composeRuntime)
	api(libs.dagger.core)
	api(libs.inject)
	api(libs.kotlinx.collectionsImmutableJvm)
	api(libs.kotlinx.coroutinesCore)
	api(libs.kotlinx.serializationCore)

	implementation(libs.androidx.annotation)
	implementation(libs.androidx.composeMaterial3)
	implementation(libs.androidx.composeMaterialIconsCore)
	implementation(libs.androidx.composeUi)
	implementation(libs.androidx.composeUiGeometry)
	implementation(libs.androidx.composeUiGraphics)
	implementation(libs.androidx.composeUiText)
	implementation(libs.androidx.composeUiUnit)
	implementation(libs.androidx.composeUiTooling)
	implementation(libs.androidx.composeUiToolingPreview)
	implementation(libs.androidx.composeUiUtil) // transitive
	implementation(libs.androidx.paletteKtx)
	implementation(libs.coil.base)
	implementation(libs.coil.compose)
	implementation(libs.coil.compose.base)
	implementation(libs.dagger.hiltAndroid)
	implementation(libs.dagger.hiltCore)
	implementation(libs.kotlin.parcelizeRuntime)
	implementation(libs.kotlinResult.result)
	implementation(libs.timber)

	testFixturesImplementation(platform(libs.androidx.compose.bom))

	testFixturesImplementation(libs.androidx.activity)
	testFixturesImplementation(libs.androidx.composeUiTest)
	testFixturesImplementation(libs.androidx.testExtJUnit)
	testFixturesImplementation(libs.androidx.composeUiTestJunit4Android)

	kspTest(libs.dagger.compiler)

	add("ksp", libs.dagger.compiler)
	add("ksp", libs.androidx.hiltCompiler)
	add("ksp", libs.dagger.hiltAndroidCompiler)

	kspTest(libs.androidx.hiltCompiler)
	kspTest(libs.dagger.hiltAndroidCompiler)

	screenshotTestImplementation(libs.androidx.composeUiTooling)

}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	compilerOptions.freeCompilerArgs.add("-opt-in=androidx.compose.material3.ExperimentalMaterial3Api")
}
