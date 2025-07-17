plugins {
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.dagger.hiltAndroid)
	alias(libs.plugins.ksp)
	alias(libs.plugins.kotlin.serialization)
	id("android-library-convention")
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
	testFixtures {
		enable = true
	}
}

dependencies {
	api(project(":account:account-domain"))
	api(project(":shared"))

	implementation(project(":shared-data"))

	api(libs.dagger.core)
	api(libs.dagger.hiltCore)
	api(libs.dagger.hiltAndroid)
	api(libs.inject)
	api(libs.retrofit)

	implementation(libs.androidx.coreKtx) //transitive
	implementation(libs.kotlinx.serializationJson)
	implementation(libs.androidx.roomRuntime)
	implementation(libs.autobind.android.api)
	implementation(libs.autobind.core) //transitive
	implementation(libs.kotlinx.serializationCore)
	implementation(libs.kotlinResult.result)
	implementation(libs.okhttp3)
	implementation(libs.timber)

	ksp(libs.androidx.hiltCompiler)
	ksp(libs.androidx.roomCompiler)
	ksp(libs.autobind.compiler)
	ksp(libs.dagger.compiler)
	ksp(libs.dagger.hiltCompiler)

	testImplementation(libs.jUnit)
	testImplementation(libs.kotest.assertionsCore)
	testImplementation(libs.kotest.assertionsShared)
	testImplementation(libs.kotlinx.coroutinesTest)
	testImplementation(libs.kotlinx.serializationJson)
	testImplementation(libs.mockk.core)
	testImplementation(libs.mockk.dsl) //transitive
	testImplementation(libs.okhttp3.mockWebServer)
	testImplementation(libs.retrofit.converterKotlinxSerialization)
	testImplementation(libs.robolectric)
	testImplementation(testFixtures(project("::account:account-domain")))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	compilerOptions.freeCompilerArgs.add("-opt-in=okhttp3.ExperimentalOkHttpApi")
}
