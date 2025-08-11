plugins {
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.dagger.hiltAndroid)
	alias(libs.plugins.ksp)
	alias(libs.plugins.kotlin.serialization)
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

	api(libs.dagger.core)
	implementation(libs.inject)
	api(libs.kotlinx.coroutinesCore)
	implementation(libs.dagger.hiltAndroid)
	implementation(libs.dagger.hiltCore)

	ksp(libs.androidx.hiltCompiler)
	ksp(libs.dagger.compiler)
	ksp(libs.dagger.hiltCompiler)

}
