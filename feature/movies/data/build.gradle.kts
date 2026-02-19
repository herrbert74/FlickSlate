plugins {
	id("android-library-convention")
	alias(libs.plugins.ksp)
	alias(libs.plugins.kotlin.serialization)
	alias(libs.plugins.room)
	id("data-convention")
	id("metro-convention")
}

android {
	namespace = "com.zsoltbertalan.flickslate.movies.data"

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}

	@Suppress("UnstableApiUsage")
	testFixtures.enable = true

	testOptions {
		unitTests.isIncludeAndroidResources = true
	}

	sourceSets {
		getByName("test").assets.srcDir("$projectDir/schemas")
	}
}

room {
	schemaDirectory("$projectDir/schemas")
}

dependencies {
	api(project(":feature:movies:domain"))

	implementation(libs.kotlinx.serializationJson)

	testImplementation(libs.kotest.assertionsCore)
	testImplementation(libs.kotest.assertionsShared)
	testImplementation(libs.mockk.dsl)
	testImplementation(libs.okhttp3.mockWebServer)
	testImplementation(libs.okio)
	testImplementation(libs.retrofit.converterKotlinxSerialization)
	testImplementation(testFixtures(project(":feature:movies:data")))
	testImplementation(testFixtures(project(":shared:data")))
	testImplementation(testFixtures(project(":shared:domain")))
	testImplementation(libs.androidx.roomTesting)
	testImplementation(libs.androidx.testCore)
	testImplementation(libs.androidx.testMonitor)
	testImplementation(libs.robolectric)

	testFixturesApi(libs.kotlin.stdlib)
	testFixturesImplementation(project(":shared:data"))
}
