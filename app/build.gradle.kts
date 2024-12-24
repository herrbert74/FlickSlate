@file:Suppress("UnstableApiUsage")

plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.jetbrains.kotlin.android)
	alias(libs.plugins.compose.compiler)
	alias(libs.plugins.dependency.analysis)
	alias(libs.plugins.serialization)
	id("kotlin-parcelize")
	alias(libs.plugins.ksp)
	alias(libs.plugins.google.dagger.hilt.android)
	alias(libs.plugins.detekt)
}

apply(from = project.rootProject.file("config/detekt/detekt.gradle"))

val tmdbApiKey: String by project

android {

	namespace = "com.zsoltbertalan.flickslate"

	defaultConfig {
		applicationId = "com.zsoltbertalan.flickslate"
		versionCode = libs.versions.versionCode.get().toInt()
		versionName = libs.versions.versionName.toString()
		vectorDrawables.useSupportLibrary = true
		minSdk = libs.versions.minSdkVersion.get().toInt()
		compileSdk = libs.versions.compileSdkVersion.get().toInt()
		targetSdk = libs.versions.targetSdkVersion.get().toInt()
		buildConfigField("String", "TMDB_API_KEY", tmdbApiKey)
		testInstrumentationRunner = "com.zsoltbertalan.flickslate.FlickSlateAndroidJUnitRunner"
	}

	buildTypes {

		getByName("release") {
			isDebuggable = false
			isMinifyEnabled = true
			proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
		}

		getByName("debug") {
			isMinifyEnabled = false
		}

	}

	buildFeatures {
		buildConfig = true
		compose = true
	}

	//Needed for Mockk
	testOptions {
		packaging { jniLibs { useLegacyPackaging = true } }
		unitTests.isReturnDefaultValues = true
	}
	packaging {
		resources.excludes.add("MANIFEST.MF")
		resources.excludes.add("META-INF/LICENSE")
		resources.excludes.add("META-INF/LICENSE.txt")
		resources.excludes.add("META-INF/LICENSE.md")
		resources.excludes.add("META-INF/LICENSE-notice.md")
		resources.excludes.add("META-INF/MANIFEST.MF")
		resources.excludes.add("META-INF/NOTICE.txt")
		resources.excludes.add("META-INF/rxjava.properties")
		resources.excludes.add("jsr305_annotations/Jsr305_annotations.gwt.xml")
	}

}

kotlin {
	jvmToolchain(21)
}

dependencies {

	implementation(project(":account:account-data"))
	implementation(project(":account:account-domain"))
	implementation(project(":account:account-ui"))
	implementation(project(":movies:movies-data"))
	implementation(project(":movies:movies-domain"))
	implementation(project(":movies:movies-ui"))
	implementation(project(":search:search-data"))
	implementation(project(":search:search-domain"))
	implementation(project(":search:search-ui"))
	implementation(project(":shared"))
	implementation(project(":shared-data"))
	implementation(project(":tv:tv-data"))
	implementation(project(":tv:tv-domain"))
	implementation(project(":tv:tv-ui"))

	implementation(libs.androidx.activity.activity)
	implementation(libs.androidx.activity.compose)
	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.compose.animation)
	implementation(libs.androidx.compose.animation.core)
	implementation(libs.androidx.compose.foundation)
	implementation(libs.androidx.compose.foundationLayout)
	implementation(libs.androidx.compose.runtime)
	implementation(libs.androidx.compose.runtime.saveable)
	implementation(libs.androidx.compose.ui.graphics)
	implementation(libs.androidx.compose.ui.text)
	implementation(libs.androidx.compose.ui.unit)
	implementation(libs.androidx.compose.ui.tooling)
	implementation(libs.androidx.compose.ui.toolingPreview)
	implementation(libs.androidx.compose.ui.ui)
	implementation(libs.androidx.compose.material3)
	implementation(libs.androidx.hilt.navigation.compose)
	implementation(libs.androidx.lifecycle.common)
	implementation(libs.androidx.lifecycle.runtime.compose)
	implementation(libs.androidx.lifecycle.viewmodel)
	implementation(libs.androidx.lifecycle.viewmodel.compose)
	implementation(libs.androidx.lifecycle.viewmodel.savedstate)
	implementation(libs.androidx.navigation.common)
	implementation(libs.androidx.navigation.compose)
	implementation(libs.androidx.navigation.runtime)
	implementation(libs.androidx.room.runtime)
	implementation(libs.google.dagger.core)
	implementation(libs.google.dagger.hilt.core)
	implementation(libs.google.dagger.hilt.android)
	implementation(libs.inject)
	implementation(libs.kotlinResult.result)
	implementation(libs.kotlinx.coroutines.core)
	implementation(libs.kotlinx.serialization.core)
	implementation(libs.kotlinx.serialization.json)
	implementation(libs.squareUp.okhttp3.okhttp)
	implementation(libs.squareUp.retrofit2.retrofit)
	implementation(libs.timber)

	debugRuntimeOnly(platform(libs.androidx.compose.bom))
	//Needed for createComposeRule, NOT ONLY for createAndroidComposeRule, as in the docs
	debugRuntimeOnly(libs.androidx.compose.ui.testManifest)

	add("ksp", libs.google.dagger.hilt.androidCompiler)

	testImplementation(libs.junit)
	testImplementation(libs.test.kotest.assertions.shared)
	testImplementation(libs.test.mockk.core)
	testImplementation(libs.kotlinx.coroutines.test)
	testImplementation(libs.test.kotest.assertions.core)

	kspTest(libs.google.dagger.compiler)
	kspTest(libs.androidx.hilt.compiler)
	kspTest(libs.google.dagger.hilt.androidCompiler)

	androidTestImplementation(testFixtures(project("::movies:movies-domain")))
	androidTestImplementation(testFixtures(project("::tv:tv-domain")))
	androidTestImplementation(testFixtures(project("::shared")))
	androidTestImplementation(libs.androidx.test.coreKtx)
	androidTestImplementation(libs.androidx.test.ext.jUnit)
	androidTestImplementation(libs.androidx.test.runner)
	androidTestImplementation(libs.androidx.compose.ui.test.android)
	androidTestImplementation(libs.androidx.compose.ui.test.junit4)
	androidTestImplementation(libs.androidx.compose.ui.test.junit4.android)
	androidTestImplementation(libs.autobind.android.api)
	androidTestImplementation(libs.autobind.android.testing)
	androidTestImplementation(libs.google.dagger.hilt.androidTesting)
	androidTestImplementation(libs.junit)
	androidTestImplementation(libs.test.mockk.android)
	androidTestImplementation(libs.test.mockk.core)
	androidTestImplementation(libs.test.mockk.dsl)

	kspAndroidTest(libs.google.dagger.compiler)
	kspAndroidTest(libs.google.dagger.hilt.androidCompiler)
	kspAndroidTest(libs.autobind.compiler)

	detektPlugins(libs.detekt.compose)

}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	compilerOptions.freeCompilerArgs.add("-opt-in=androidx.compose.material3.ExperimentalMaterial3Api")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	compilerOptions.freeCompilerArgs.add("-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi")
}
