plugins {
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.ksp)
	alias(libs.plugins.kotlin.serialization)
	id("kotlin-parcelize")
	id("android-library-convention")
}

android {
	namespace = "com.zsoltbertalan.flickslate.shared.domain"

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}

	@Suppress("UnstableApiUsage")
	testFixtures {
		enable = true
	}

}

dependencies {
	implementation(platform(libs.androidx.compose.bom))

	api(libs.kotlinx.serializationCore)

	implementation(libs.kotlin.parcelizeRuntime)
	implementation(libs.timber)

	kspTest(libs.dagger.compiler)

	ksp(libs.dagger.compiler)
	ksp(libs.androidx.hiltCompiler)
	ksp(libs.dagger.hiltAndroidCompiler)

	kspTest(libs.androidx.hiltCompiler)
	kspTest(libs.dagger.hiltAndroidCompiler)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	compilerOptions.freeCompilerArgs.add("-opt-in=androidx.compose.material3.ExperimentalMaterial3Api")
}
