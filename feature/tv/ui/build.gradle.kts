plugins {
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.kotlin.composeCompiler)
	alias(libs.plugins.ksp)
	id("android-library-convention")
	id("coil-convention")
	id("dagger-convention")
	id("ui-convention")
}

android {
	namespace = "com.zsoltbertalan.flickslate.tv.ui"

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}
}

dependencies {
	api(project(":feature:account:domain"))
	api(project(":feature:tv:domain"))

	api(libs.androidx.lifecycleCommon)
	api(libs.androidx.lifecycleViewmodelCompose)

	implementation(libs.androidx.composeUiToolingPreview)
	implementation(libs.androidx.coreKtx) // transitive
	implementation(libs.androidx.lifecycleRuntimeCompose)
	implementation(libs.androidx.composeMaterialIconsExtended)

	testImplementation(testFixtures(project(":feature:tv:domain")))

	testImplementation(libs.kotest.assertionsCore)
	testImplementation(libs.mockk.dsl) // transitive

}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	compilerOptions.freeCompilerArgs.add("-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi")
}
