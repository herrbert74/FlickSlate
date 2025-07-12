plugins {
	id("com.android.library")
	kotlin("android")
	alias(libs.plugins.ksp)
}

android {
	buildFeatures {
		compose = true
	}
}

dependencies {
	api(project(":shared"))

	implementation(platform(libs.androidx.compose.bom))

	implementation(libs.androidx.composeFoundation)
	implementation(libs.androidx.composeFoundationLayout)
	implementation(libs.androidx.composeRuntimeSaveable)
	implementation(libs.androidx.composeUi)
	implementation(libs.androidx.composeUiGraphics)
	implementation(libs.androidx.composeUiText)
	implementation(libs.androidx.composeUiUnit)
	implementation(libs.androidx.composeUiTooling)
//	implementation(libs.androidx.compose.ui.toolingPreview)
	implementation(libs.androidx.composeMaterialIconsCore)
	implementation(libs.androidx.composeMaterial3)
	api(libs.androidx.composeRuntime)
	api(libs.androidx.lifecycleViewmodel)
	api(libs.androidx.lifecycleViewmodelSavedstate)
	implementation(libs.kotlinx.collectionsImmutableJvm)
	api(libs.kotlinx.coroutinesCore)
	implementation(libs.coil.compose)
	implementation(libs.coil.compose.base)
	api(libs.dagger.core)
	implementation(libs.dagger.hiltAndroid)
	api(libs.inject)
	implementation(libs.kotlinResult.result)
	implementation(libs.timber)

	ksp(libs.androidx.hiltCompiler)
	ksp(libs.androidx.roomCompiler)
	ksp(libs.dagger.compiler)
	ksp(libs.dagger.hiltCompiler)
	ksp(libs.dagger.hiltAndroidCompiler)

	testImplementation(libs.jUnit)
	testImplementation(libs.kotest.assertionsShared)
	testImplementation(libs.kotest.assertionsCore)
	testImplementation(libs.mockk.core)
	testImplementation(libs.kotlinx.coroutinesTest)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	compilerOptions.freeCompilerArgs.add("-opt-in=androidx.compose.material3.ExperimentalMaterial3Api")
}
