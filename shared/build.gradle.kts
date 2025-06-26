plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.compose.compiler)
	alias(libs.plugins.compose.screenshot.testing)
	alias(libs.plugins.detekt)
	alias(libs.plugins.google.dagger.hilt.android)
	alias(libs.plugins.jetbrains.kotlin.android)
	alias(libs.plugins.ksp)
	alias(libs.plugins.serialization)
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
	kotlinOptions {
		jvmTarget = "21"
	}

	@Suppress("UnstableApiUsage")
	testFixtures {
		enable = true
	}

	experimentalProperties["android.experimental.enableScreenshotTest"] = true
}

dependencies {
	implementation(libs.androidx.annotation)
	implementation(platform(libs.androidx.compose.bom))
	api(libs.androidx.compose.foundation)
	api(libs.androidx.compose.foundationLayout)
	api(libs.androidx.compose.runtime)
	implementation(libs.androidx.compose.ui.ui)
	implementation(libs.androidx.compose.ui.geometry)
	implementation(libs.androidx.compose.ui.graphics)
	implementation(libs.androidx.compose.ui.text)
	implementation(libs.androidx.compose.ui.unit)
	implementation(libs.androidx.compose.ui.tooling)
	implementation(libs.androidx.compose.ui.toolingPreview)
	implementation(libs.androidx.compose.material3)
	implementation(libs.androidx.palette.ktx)
	implementation(libs.coil.base)
	implementation(libs.coil.compose)
	implementation(libs.coil.compose.base)
	api(libs.google.dagger.core)
	implementation(libs.google.dagger.hilt.android)
	implementation(libs.google.dagger.hilt.core)
	api(libs.inject)
	implementation(libs.kotlin.parcelize.runtime)
	implementation(libs.kotlinResult.result)
	implementation(libs.kotlinx.collections.immutable.jvm)
	api(libs.kotlinx.coroutines.core)
	api(libs.kotlinx.serialization.core)
	implementation(libs.timber)
	implementation(libs.androidx.coreKtx)
	testFixturesImplementation(platform(libs.androidx.compose.bom))
	testFixturesImplementation(libs.androidx.compose.ui.test.junit4.android)

	kspTest(libs.google.dagger.compiler)

	add("ksp", libs.google.dagger.compiler)
	add("ksp", libs.androidx.hilt.compiler)
	add("ksp", libs.google.dagger.hilt.androidCompiler)

	kspTest(libs.androidx.hilt.compiler)
	kspTest(libs.google.dagger.hilt.androidCompiler)

	detektPlugins(libs.detekt.compose)

	screenshotTestImplementation(libs.compose.screenshot.testing.api)
	screenshotTestImplementation(libs.androidx.compose.ui.tooling)

}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	compilerOptions.freeCompilerArgs.add("-opt-in=androidx.compose.material3.ExperimentalMaterial3Api")
}
