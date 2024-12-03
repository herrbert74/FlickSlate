plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.compose.compiler)
	alias(libs.plugins.jetbrains.kotlin.android)
	alias(libs.plugins.ksp)
	id("android-library-convention")
}

android {
	namespace = "com.zsoltbertalan.flickslate.movies.ui"
	compileSdk = 34

	defaultConfig {
		minSdk = 24

		consumerProguardFiles("consumer-rules.pro")
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}
	buildFeatures {
		compose = true
	}
}

kotlin {
	jvmToolchain(21)
}

dependencies {

	implementation(project(":movies:movies-domain"))
	implementation(project(":shared"))
	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.compose.foundation)
	implementation(libs.androidx.compose.ui.ui)
	implementation(libs.androidx.compose.ui.graphics)
	implementation(libs.androidx.compose.ui.text)
	implementation(libs.androidx.compose.ui.unit)
	implementation(libs.androidx.compose.ui.tooling)
	implementation(libs.androidx.compose.ui.toolingPreview)
	implementation(libs.androidx.compose.material3)
	implementation(libs.androidx.coreKtx)
	implementation(libs.androidx.hilt.navigation.compose)
	implementation(libs.androidx.lifecycle.runtime)
	implementation(libs.androidx.lifecycle.runtime.compose)
	implementation(libs.androidx.navigation.common)
	implementation(libs.androidx.navigation.compose)
	implementation(libs.coil)
	implementation(libs.google.dagger.hilt.android)
	implementation(libs.google.material)
	implementation(libs.kotlinResult.result)
	implementation(libs.kotlinResult.coroutines)
	implementation(libs.timber)

	add("ksp", libs.google.dagger.hilt.androidCompiler)

	testImplementation(libs.junit)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	compilerOptions.freeCompilerArgs.add("-opt-in=androidx.compose.material3.ExperimentalMaterial3Api")
}