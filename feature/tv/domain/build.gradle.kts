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
		testFixtures {
			enable = true
		}
	}
}

dependencies {

	api(project(":shared:domain"))
	api(libs.kotlinx.collectionsImmutableJvm)
	api(libs.jakarta.inject)

	implementation(libs.kotlinResult.result)
	implementation(libs.kotlinx.coroutinesCore)
	"testFixturesApi"(project(":shared:domain"))

	testFixturesCompileOnly(libs.kotlinx.collectionsImmutableJvm)

}
