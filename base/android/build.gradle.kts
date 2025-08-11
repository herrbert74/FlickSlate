plugins {
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.dagger.hiltAndroid)
	alias(libs.plugins.ksp)
	alias(libs.plugins.kotlin.serialization)
	id("kotlin-parcelize")
	id("android-library-convention")
}

android {
	namespace = "com.zsoltbertalan.flickslate.base.android"

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}
}

dependencies {
	implementation(project(":base:kotlin"))
	implementation(platform(libs.androidx.compose.bom))

	api(libs.androidx.composeFoundation)
	api(libs.androidx.composeFoundationLayout)
	api(libs.androidx.composeRuntime)
	api(libs.dagger.core)
	api(libs.inject)
	api(libs.kotlinx.collectionsImmutableJvm)
	api(libs.kotlinx.coroutinesCore)
	api(libs.kotlinx.serializationCore)

	implementation(libs.androidx.annotation)
	implementation(libs.dagger.hiltAndroid)
	implementation(libs.dagger.hiltCore)
	implementation(libs.kotlin.parcelizeRuntime)
	implementation(libs.kotlinResult.result)

	ksp(libs.androidx.hiltCompiler)
	ksp(libs.dagger.compiler)
	ksp(libs.dagger.hiltCompiler)

}
