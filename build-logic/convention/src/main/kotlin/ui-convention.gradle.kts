plugins {
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.ksp)
}

android {
	buildFeatures {
		compose = true
	}
}

dependencies {
	api(project(":shared:domain"))
	api(project(":shared:ui"))

	implementation(platform(libs.androidx.compose.bom))

	api(libs.androidx.composeRuntime)
	api(libs.androidx.lifecycleViewmodel)
	api(libs.androidx.lifecycleViewmodelSavedstate)
	api(libs.dagger.core)
	api(libs.inject)
	api(libs.kotlinx.coroutinesCore)

	implementation(libs.androidx.composeFoundation)
	implementation(libs.androidx.composeFoundationLayout)
	implementation(libs.androidx.composeMaterial3)
	implementation(libs.androidx.composeRuntimeSaveable)
	implementation(libs.androidx.composeUi)
	implementation(libs.androidx.composeUiGraphics)
	implementation(libs.androidx.composeUiText)
	implementation(libs.androidx.composeUiUnit)
	implementation(libs.androidx.composeUiTooling)
	implementation(libs.coil.compose)
	implementation(libs.coil.compose.base)
	implementation(libs.dagger.hiltAndroid)
	implementation(libs.kotlinResult.result)
	implementation(libs.kotlinx.collectionsImmutableJvm)
	implementation(libs.timber)

	ksp(libs.androidx.hiltCompiler)
	ksp(libs.androidx.roomCompiler)
	ksp(libs.dagger.compiler)
	ksp(libs.dagger.hiltCompiler)
	ksp(libs.dagger.hiltAndroidCompiler)

	testImplementation(testFixtures(project(":shared:ui")))
	testImplementation(libs.jUnit)
	testImplementation(libs.mockk.core)
	testImplementation(libs.kotlinx.coroutinesTest)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	compilerOptions.freeCompilerArgs.add("-opt-in=androidx.compose.material3.ExperimentalMaterial3Api")
}
