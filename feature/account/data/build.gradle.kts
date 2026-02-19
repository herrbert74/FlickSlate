plugins {
	id("android-library-convention")
	alias(libs.plugins.ksp)
	alias(libs.plugins.kotlin.serialization)
	alias(libs.plugins.room)
	id("data-convention")
	id("metro-convention")
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

room {
	schemaDirectory("$projectDir/schemas")
}

dependencies {
	api(project(":feature:account:domain"))

	api(libs.retrofit)

	implementation(libs.androidx.coreKtx) // transitive
	implementation(libs.kotlinx.serializationJson)
	implementation(libs.androidx.roomRuntime)
	implementation(libs.kotlinx.serializationCore)
	implementation(libs.kotlinResult.result)
	implementation(libs.okhttp3)
	implementation(libs.timber)

	ksp(libs.androidx.roomCompiler)

	testImplementation(libs.jUnit)
	testImplementation(libs.kotest.assertionsCore)
	testImplementation(libs.kotlinx.coroutinesTest)
	testImplementation(libs.kotlinx.serializationJson)
	testImplementation(libs.okhttp3.mockWebServer)
	testImplementation(libs.retrofit.converterKotlinxSerialization)
	testImplementation(libs.robolectric)
	testImplementation(testFixtures(project(":feature:account:domain")))
	testImplementation(testFixtures(project(":shared:data")))
}
