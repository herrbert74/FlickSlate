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
	if (project.parent?.name == "tv") {
		api(project(":base:kotlin"))
	} else {
		implementation(project(":base:kotlin"))
	}

	api(project(":shared:domain"))

	if (project.parent?.name == "account") {
		implementation(project(":shared:ui"))
	} else {
		api(project(":shared:ui"))
	}

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

	excludeFrom(listOf("account")) {
		implementation(libs.androidx.composeRuntimeSaveable)
	}

	excludeFrom(listOf("search")) {
		implementation(libs.androidx.hiltLifeCycleViewModelCompose)
	}

	implementation(libs.androidx.composeUi)
	implementation(libs.androidx.composeUiGraphics)
	implementation(libs.androidx.composeUiText)
	implementation(libs.androidx.composeUiUnit)
	implementation(libs.androidx.composeUiTooling)
	implementation(libs.dagger.hiltAndroid)
	implementation(libs.dagger.hiltCore)
	implementation(libs.kotlinResult.result)

	if (project.parent?.name == "search" || project.parent?.name == "account") {
		api(libs.kotlinx.collectionsImmutableJvm)
	} else {
		implementation(libs.kotlinx.collectionsImmutableJvm)
	}

	implementation(libs.timber)

	testImplementation(libs.jUnit)
	testImplementation(libs.mockk.core)
	testImplementation(libs.kotlinx.coroutinesTest)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	compilerOptions.freeCompilerArgs.add("-opt-in=androidx.compose.material3.ExperimentalMaterial3Api")
}

fun excludeFrom(excluded: List<String>, dependency: () -> Unit) {
	if (!(excluded.contains(project.parent?.name))) {
		dependency()
	}
}
