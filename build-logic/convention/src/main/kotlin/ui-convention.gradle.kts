plugins {
	alias(libs.plugins.androidLibrary)
}

// android {
extensions.configure<com.android.build.api.dsl.LibraryExtension>("android") {
	buildFeatures {
		compose = true
	}
}

dependencies {
	if (project.parent?.name == "tv") {
		api(project(":base:kotlin"))
	} else {
		implementation(project(":base:kotlin"))
	}

	api(project(":shared:domain"))
	api(project(":shared:ui"))

	implementation(platform(libs.androidx.compose.bom))

	api(libs.androidx.composeRuntime)
	api(libs.androidx.lifecycleViewmodel)

	excludeFrom(listOf("account")) {
		api(libs.androidx.lifecycleViewmodelSavedstate)
	}

	api(libs.kotlinx.coroutinesCore)

	implementation(libs.androidx.composeFoundation)
	implementation(libs.androidx.composeFoundationLayout)
	implementation(libs.androidx.composeMaterial3)

	excludeFrom(listOf("account")) {
		implementation(libs.androidx.composeRuntimeAnnotation)
	}

	implementation(libs.androidx.composeRuntimeSaveable)

	excludeFrom(listOf("search")) {
		implementation(libs.androidx.hiltLifeCycleViewModelCompose)
	}

	if (project.parent?.name != "account") {
		implementation(libs.androidx.composeMaterialIconsCore)
	}
	implementation(libs.androidx.composeUi)
	implementation(libs.androidx.composeUiGraphics)
	implementation(libs.androidx.composeUiText)
	implementation(libs.androidx.composeUiUnit)
	implementation(libs.androidx.composeUiTooling)
	implementation(libs.dagger.hiltAndroid)
	implementation(libs.dagger.hiltCore)
	implementation(libs.kotlinResult.result)

	if (project.parent?.name == "search") {
		api(libs.kotlinx.collectionsImmutableJvm)
	} else if (project.parent?.name != "account") {
		implementation(libs.kotlinx.collectionsImmutableJvm)
	}

	implementation(libs.timber)

	testImplementation(libs.jUnit)
	testImplementation(libs.mockk.core)
	testImplementation(libs.kotlinx.coroutinesTest)

	if (project.parent?.name != "search") {
		testImplementation(libs.kotest.assertionsShared)
	}
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	compilerOptions.freeCompilerArgs.add("-opt-in=androidx.compose.material3.ExperimentalMaterial3Api")
}

fun excludeFrom(excluded: List<String>, dependency: () -> Unit) {
	if (!(excluded.contains(project.parent?.name))) {
		dependency()
	}
}
