plugins {
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.kotlin.android)
}

android {
	buildFeatures {
		compose = true
	}
}

dependencies {
	api(project(":base:kotlin"))
	api(project(":shared:domain"))
	api(project(":shared:ui"))

	implementation(platform(libs.androidx.compose.bom))

	api(libs.androidx.composeRuntime)
	api(libs.androidx.lifecycleViewmodel)
	api(libs.androidx.lifecycleViewmodelSavedstate)

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
	implementation(libs.dagger.hiltAndroid)
	implementation(libs.kotlinResult.result)
	implementation(libs.kotlinx.collectionsImmutableJvm)
	implementation(libs.timber)

	testImplementation(libs.jUnit)
	testImplementation(libs.mockk.core)
	testImplementation(libs.kotlinx.coroutinesTest)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	compilerOptions.freeCompilerArgs.add("-opt-in=androidx.compose.material3.ExperimentalMaterial3Api")
}
