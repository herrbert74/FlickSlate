plugins {
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.kotlin.composeCompiler)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.ksp)
	id("android-library-convention")
	id("dagger-convention")
	id("ui-convention")
}

android {
	namespace = "com.zsoltbertalan.flickslate.account.ui"

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}
}

dependencies {
	api(project(":feature:account:domain"))

	api(libs.androidx.composeRuntime)
	api(libs.androidx.lifecycleViewmodel)
	api(libs.kotlinx.coroutinesCore)

	implementation(platform(libs.androidx.compose.bom))

	implementation(libs.androidx.composeFoundation)
	implementation(libs.androidx.composeFoundationLayout)
	implementation(libs.androidx.composeMaterial3)
	implementation(libs.androidx.composeUi)
	implementation(libs.androidx.composeUiGraphics)
	implementation(libs.androidx.composeUiText)
	implementation(libs.androidx.composeUiUnit)
	implementation(libs.androidx.composeUiTooling)
	implementation(libs.androidx.composeUiToolingPreview)
	implementation(libs.androidx.lifecycleViewmodelCompose)
	implementation(libs.kotlinResult.coroutines)

	implementation(libs.dagger.hiltAndroid)
	implementation(libs.kotlinResult.result)
	implementation(libs.timber)

	testImplementation(project(":feature:tv:domain"))
	testImplementation(testFixtures(project(":feature:account:domain")))
	testImplementation(testFixtures(project(":feature:tv:domain")))

	testImplementation(libs.kotest.assertionsCore)
	testImplementation(libs.mockk.dsl) // transitive

}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	compilerOptions.freeCompilerArgs.add("-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi")
}
