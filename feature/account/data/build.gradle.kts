plugins {
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.dagger.hiltAndroid)
	alias(libs.plugins.ksp)
	alias(libs.plugins.kotlin.serialization)
	id("android-library-convention")
	id("dagger-convention")
	id("data-convention")
}

android {
	namespace = "com.zsoltbertalan.flickslate.account.data"

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}

	@Suppress("UnstableApiUsage")
	testFixtures.enable = true
}

dependencies {
	api(project(":feature:account:domain"))
	implementation(project(":shared:domain"))

	implementation(project(":shared:data"))

	api(libs.dagger.hiltCore)
	implementation(libs.dagger.hiltAndroid)
	api(libs.retrofit)

	implementation(libs.androidx.coreKtx) // transitive
	implementation(libs.kotlinx.serializationJson)
	implementation(libs.androidx.roomRuntime)
	implementation(libs.autobind.android.api)
	implementation(libs.autobind.core) // transitive
	implementation(libs.kotlinx.serializationCore)
	implementation(libs.kotlinResult.result)
	implementation(libs.okhttp3)
	implementation(libs.timber)

	ksp(libs.androidx.roomCompiler)
	ksp(libs.autobind.compiler)

	testImplementation(libs.jUnit)
	testImplementation(libs.kotest.assertionsCore)
	testImplementation(libs.kotlinx.coroutinesTest)
	testImplementation(libs.kotlinx.serializationJson)
	testImplementation(libs.mockk.core)
	testImplementation(libs.mockk.dsl) // transitive
	testImplementation(libs.okhttp3.mockWebServer)
	testImplementation(libs.retrofit.converterKotlinxSerialization)
	testImplementation(libs.robolectric)
	testImplementation(testFixtures(project(":feature:account:domain")))
	testImplementation(testFixtures(project(":shared:data")))
}
