plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.detekt)
	alias(libs.plugins.jetbrains.kotlin.android)
	alias(libs.plugins.serialization)
	alias(libs.plugins.ksp)
	id("android-library-convention")
}

apply(from = project.rootProject.file("config/detekt/detekt.gradle"))

val tmdbApiKey: String by project

android {
	namespace = "com.zsoltbertalan.flickslate.shared.data"

	defaultConfig {
		buildConfigField("String", "TMDB_API_KEY", tmdbApiKey)
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}

	buildFeatures {
		buildConfig = true
	}
}

dependencies {
	api(project(":shared"))

	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.compose.ui.tooling)
	implementation(libs.baBeStudios.base.data)
	api(libs.google.dagger.core)
	api(libs.inject)
	implementation(libs.kotlinResult.result)
	api(libs.kotlinx.collections.immutable.jvm)
	api(libs.kotlinx.coroutines.core)
	api(libs.kotlinx.serialization.core)
	implementation(libs.kotlinx.serialization.json)
	api(libs.squareUp.okhttp3.okhttp)
	implementation(libs.squareUp.okhttp3.loggingInterceptor)
	implementation(libs.squareUp.retrofit2.converterKotlinxSerialization)
	api(libs.squareUp.retrofit2.retrofit)
	implementation(libs.timber)
	implementation(libs.google.dagger.hilt.core)

	add("ksp", libs.google.dagger.compiler)
	add("ksp", libs.androidx.hilt.compiler)
	add("ksp", libs.google.dagger.hilt.androidCompiler)

	kspTest(libs.google.dagger.compiler)
	kspTest(libs.androidx.hilt.compiler)
	kspTest(libs.google.dagger.hilt.androidCompiler)

	detektPlugins(libs.detekt.compose)


}