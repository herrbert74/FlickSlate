plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.jetbrains.kotlin.android)
	alias(libs.plugins.google.dagger.hilt.android)
	alias(libs.plugins.ksp)
	alias(libs.plugins.serialization)
	id("android-library-convention")
}

android {
	namespace = "com.zsoltbertalan.flickslate.movies.data"
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
	implementation(project(":movies:movies-domain"))
	implementation(project(":shared"))
	implementation(project(":shared-data"))

	implementation(libs.baBeStudios.base.data)
	implementation(libs.squareUp.retrofit2.retrofit)
	implementation(libs.androidx.room.common)
	implementation(libs.androidx.room.ktx)
	implementation(libs.androidx.room.runtime)
	add("ksp",libs.androidx.room.compiler)
	implementation(libs.inject)
	implementation(libs.google.dagger.core)
	add("ksp", libs.google.dagger.compiler)

	implementation(libs.kotlinx.serialization.json)
	implementation(libs.google.dagger.hilt.android)
	add("ksp", libs.androidx.hilt.compiler)
	add("ksp", libs.google.dagger.hilt.compiler)

	implementation(libs.kotlinResult.result)
	implementation(libs.kotlinResult.coroutines)
	implementation(libs.timber)

	implementation(libs.androidx.coreKtx)
	implementation(libs.androidx.appcompat)
	implementation(libs.google.material)
	testImplementation(libs.junit)
}