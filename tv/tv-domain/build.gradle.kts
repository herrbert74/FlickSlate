plugins {
	id("android-library-convention")
	alias(libs.plugins.serialization)
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

	api(project(":shared"))

	implementation(libs.kotlinResult.result)
	implementation(libs.kotlinx.collections.immutable.jvm)
	implementation(libs.kotlinx.coroutines.core)

	testFixturesCompileOnly(libs.kotlinx.collections.immutable.jvm)

}
