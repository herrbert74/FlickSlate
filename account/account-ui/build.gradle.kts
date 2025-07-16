plugins {
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.kotlin.composeCompiler)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.ksp)
	id("android-library-convention")
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
	api(project(":account:account-domain"))
	api(project(":shared")) // transitive

	api(libs.androidx.composeRuntime) // transitive
	api(libs.androidx.lifecycleViewmodel) // transitive
	api(libs.dagger.core)
	api(libs.inject)
	api(libs.kotlinx.coroutinesCore) // transitive

	implementation(platform(libs.androidx.compose.bom))

	implementation(libs.androidx.composeFoundation)
	implementation(libs.androidx.composeFoundationLayout)
	implementation(libs.androidx.composeMaterial3)
	implementation(libs.androidx.composeUi)
	implementation(libs.androidx.composeUiGraphics)
	implementation(libs.androidx.composeUiText)
	implementation(libs.androidx.composeUiUnit)
	implementation(libs.androidx.composeUiTooling)
	implementation(libs.androidx.composeUiToolingPreview) // transitive

	implementation(libs.dagger.hiltAndroid)
	implementation(libs.kotlinResult.result)
	implementation(libs.timber)

	ksp(libs.androidx.hiltCompiler)
	ksp(libs.androidx.roomCompiler)
	ksp(libs.dagger.compiler)
	ksp(libs.dagger.hiltCompiler)
	ksp(libs.dagger.hiltAndroidCompiler)

	//testImplementation(testFixtures(project("::account:account-domain")))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	compilerOptions.freeCompilerArgs.add("-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi")
}
