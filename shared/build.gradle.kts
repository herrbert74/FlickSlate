plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.compose.compiler)
	alias(libs.plugins.detekt)
	alias(libs.plugins.google.dagger.hilt.android)
	alias(libs.plugins.jetbrains.kotlin.android)
	alias(libs.plugins.ksp)
	alias(libs.plugins.serialization)
	id("kotlin-parcelize")
	id("android-library-convention")
}

apply(from = project.rootProject.file("config/detekt/detekt.gradle"))

android {
	namespace = "com.zsoltbertalan.flickslate.shared"
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
}
kotlin {
	jvmToolchain(21)
}
dependencies {
	implementation(libs.baBeStudios.base.android)
	implementation(libs.baBeStudios.base.compose)
	implementation(libs.baBeStudios.base.data)
	implementation(libs.baBeStudios.base.kotlin)
	implementation(libs.androidx.activity.compose)
	implementation(libs.androidx.appcompat)
	implementation(platform(libs.androidx.compose.bom))
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
	implementation(libs.google.material)
	implementation(libs.androidx.coreKtx)
	implementation(libs.kotlinResult.result)
	implementation(libs.kotlinResult.coroutines)
	implementation(libs.kotlinx.serialization.json)
	implementation(libs.kotlin.parcelize.runtime)
	implementation(libs.timber)

	implementation(libs.google.gson)
	implementation(libs.squareUp.retrofit2.retrofit)
	implementation(libs.squareUp.retrofit2.converterKotlinxSerialization)
	implementation(libs.androidx.palette.ktx)

	implementation(libs.google.dagger.core)
	add("ksp", libs.google.dagger.compiler)
	kspTest(libs.google.dagger.compiler)
	kspAndroidTest(libs.google.dagger.compiler)

	implementation(libs.google.dagger.hilt.android)
	add("ksp", libs.androidx.hilt.compiler)
	kspTest(libs.androidx.hilt.compiler)
	add("ksp", libs.google.dagger.hilt.androidCompiler)
	kspTest(libs.google.dagger.hilt.androidCompiler)

	detektPlugins(libs.detekt.compose)
	implementation(libs.coil)

	implementation(libs.inject)

}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	compilerOptions.freeCompilerArgs.add("-opt-in=androidx.compose.material3.ExperimentalMaterial3Api")
}
