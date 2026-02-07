plugins {
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.kotlin.serialization)
	id("dagger-convention")
	id("android-library-convention")
	id("metro-convention")
}

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

		@Suppress("UnstableApiUsage")
		testFixtures.enable = true
	}
}

dependencies {

	api(project(":base:kotlin"))
	api(project(":shared:domain"))
	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.composeUiTooling)
	implementation(libs.baBeStudios.baseData)
	implementation(libs.kotlinResult.result)
	api(libs.kotlinRetry)
	api(libs.kotlinx.collectionsImmutableJvm)
	api(libs.kotlinx.coroutinesCore)
	api(libs.kotlinx.serializationCore)
	implementation(libs.kotlinx.serializationJson)
	api(libs.okhttp3)
	implementation(libs.okhttp3.loggingInterceptor)
	implementation(libs.retrofit.converterKotlinxSerialization)
	api(libs.retrofit)
	implementation(libs.timber)
	implementation(libs.dagger.hiltCore)

	testImplementation(testFixtures(project(":shared:domain")))
	testImplementation(libs.jUnit)
	testImplementation(libs.kotest.assertionsCore)
	testImplementation(libs.mockk.core)
	testImplementation(libs.mockk.dsl)
	testImplementation(libs.kotlinx.coroutinesTest)

	kspTest(libs.androidx.hiltCompiler)
	kspTest(libs.dagger.compiler)
	kspTest(libs.dagger.hiltCompiler)

	testFixturesImplementation(libs.okhttp3.mockWebServer)
	testFixturesImplementation(libs.kotlinx.serializationJson)
	testFixturesImplementation(libs.retrofit)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	compilerOptions.freeCompilerArgs.add("-opt-in=kotlin.contracts.ExperimentalContracts")
}
