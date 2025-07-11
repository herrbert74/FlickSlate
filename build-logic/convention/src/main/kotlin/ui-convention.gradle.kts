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

	implementation(libs.androidx.compose.foundation)
	implementation(libs.androidx.compose.foundationLayout)
	implementation(libs.androidx.compose.runtime.saveable)
	implementation(libs.androidx.compose.ui.ui)
	implementation(libs.androidx.compose.ui.graphics)
	implementation(libs.androidx.compose.ui.text)
	implementation(libs.androidx.compose.ui.unit)
	implementation(libs.androidx.compose.ui.tooling)
//	implementation(libs.androidx.compose.ui.toolingPreview)
	implementation(libs.androidx.compose.material.icons.core)
	implementation(libs.androidx.compose.material3)
	api(libs.androidx.compose.runtime)
	api(libs.androidx.lifecycle.viewmodel)
	api(libs.androidx.lifecycle.viewmodel.savedstate)
	implementation(libs.kotlinx.collections.immutable.jvm)
	api(libs.kotlinx.coroutines.core)
	implementation(libs.coil.compose)
	implementation(libs.coil.compose.base)
	api(libs.google.dagger.core)
	implementation(libs.google.dagger.hilt.android)
	api(libs.inject)
	implementation(libs.kotlinResult.result)
	implementation(libs.timber)

	ksp(libs.androidx.hilt.compiler)
	ksp(libs.androidx.room.compiler)
	ksp(libs.google.dagger.compiler)
	ksp(libs.google.dagger.hilt.compiler)
	ksp(libs.google.dagger.hilt.androidCompiler)

	testImplementation(libs.junit)
	testImplementation(libs.test.kotest.assertions.shared)
	testImplementation(libs.test.kotest.assertions.core)
	testImplementation(libs.test.mockk.core)
	testImplementation(libs.kotlinx.coroutines.test)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	compilerOptions.freeCompilerArgs.add("-opt-in=androidx.compose.material3.ExperimentalMaterial3Api")
}
