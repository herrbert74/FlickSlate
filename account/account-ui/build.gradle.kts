plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.compose.compiler)
	alias(libs.plugins.jetbrains.kotlin.android)
	alias(libs.plugins.ksp)
	id("android-library-convention")
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
	api(project(":account:account-domain"))
	implementation(libs.androidx.hilt.navigation.compose)
	api(libs.androidx.lifecycle.common)
	implementation(libs.androidx.lifecycle.runtime.compose)
	api(libs.androidx.lifecycle.viewmodel.compose)

	//testImplementation(testFixtures(project("::account:account-domain")))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	compilerOptions.freeCompilerArgs.add("-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi")
}
