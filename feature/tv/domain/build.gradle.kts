plugins {
	id("android-library-convention")
	alias(libs.plugins.kotlin.serialization)
}

android {
	namespace = "com.zsoltbertalan.flickslate.tv.domain"

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}

	buildFeatures {
		@Suppress("UnstableApiUsage")
		testFixtures.enable = true
	}
}

dependencies {
	api(project(":base:kotlin"))
	api(project(":shared:domain"))
	api(project(":feature:account:domain"))
	api(libs.kotlinx.collectionsImmutableJvm)
	api(libs.inject)

	implementation(libs.kotlinResult.result)
	implementation(libs.kotlinx.coroutinesCore)

	testImplementation(libs.jUnit)
	testImplementation(libs.mockk.core)
	testImplementation(libs.mockk.dsl)
	testImplementation(libs.kotest.assertionsCore)
	testImplementation(libs.kotlinx.coroutinesTest)
	testImplementation(libs.kotlinx.serializationJson)

	testFixturesApi(project(":shared:domain"))

	testFixturesCompileOnly(libs.kotlinx.collectionsImmutableJvm)

}
