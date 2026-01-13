@file:Suppress("UnstableApiUsage")

plugins {
	alias(libs.plugins.androidApplication)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.kotlin.composeCompiler)
	alias(libs.plugins.compose.screenshotTesting)
	alias(libs.plugins.dependencyAnalysis)
	alias(libs.plugins.kotlin.serialization)
	id("kotlin-parcelize")
	alias(libs.plugins.ksp)
	alias(libs.plugins.dagger.hiltAndroid)
}

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
	}

	// Needed for Mockk
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

		// Needed for Gradle since adding convention plugins the typesafe way
		resources.excludes.add("kotlin/internal/internal.kotlin_builtins")
		resources.excludes.add("kotlin/reflect/reflect.kotlin_builtins")
		resources.excludes.add("kotlin/kotlin.kotlin_builtins")
		resources.excludes.add("kotlin/coroutines/coroutines.kotlin_builtins")
		resources.excludes.add("kotlin/ranges/ranges.kotlin_builtins")
		resources.excludes.add("kotlin/collections/collections.kotlin_builtins")
		resources.excludes.add("kotlin/annotation/annotation.kotlin_builtins")
	}

	experimentalProperties["android.experimental.enableScreenshotTest"] = true

}

kotlin {
	jvmToolchain(21)
}

dependencies {

	implementation(project(":base:kotlin"))
	implementation(project(":base:android"))
	implementation(project(":feature:account:data"))
	implementation(project(":feature:account:domain"))
	implementation(project(":feature:account:ui"))
	implementation(project(":feature:movies:data"))
	implementation(project(":feature:movies:domain"))
	implementation(project(":feature:movies:ui"))
	implementation(project(":feature:search:data"))
	implementation(project(":feature:search:domain"))
	implementation(project(":feature:search:ui"))
	implementation(project(":feature:tv:data"))
	implementation(project(":feature:tv:domain"))
	implementation(project(":feature:tv:ui"))
	implementation(project(":shared:data"))
	implementation(project(":shared:domain"))
	implementation(project(":shared:ui"))

	implementation(libs.androidx.navigation3Ui)
	implementation(libs.androidx.navigation3Runtime)

	implementation(libs.androidx.activity)
	implementation(libs.androidx.activity.compose)
	implementation(libs.androidx.annotation)
	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.composeAnimation)
	implementation(libs.androidx.composeFoundation)
	implementation(libs.androidx.composeFoundationLayout)
	implementation(libs.androidx.composeRuntime)
	implementation(libs.androidx.composeRuntimeSaveable)
	implementation(libs.androidx.composeUiGraphics)
	implementation(libs.androidx.composeUiText)
	implementation(libs.androidx.composeUiUnit)
	implementation(libs.androidx.composeUiTooling)
	implementation(libs.androidx.composeUiToolingPreview)
	implementation(libs.androidx.composeUi)
	implementation(libs.androidx.composeMaterial3)
	implementation(libs.androidx.fragmentKtx) // transitive
	implementation(libs.androidx.hiltLifeCycleViewModelCompose)
	implementation(libs.androidx.lifecycleCommon)
	implementation(libs.androidx.lifecycleRuntimeCompose)
	implementation(libs.androidx.lifecycleViewmodel)
	implementation(libs.androidx.lifecycleViewmodelCompose)
	implementation(libs.androidx.lifecycleViewmodelSavedstate)
	implementation(libs.androidx.roomRuntime)
	implementation(libs.androidx.savedState)
	implementation(libs.androidx.savedStateCompose)
	implementation(libs.dagger.core)
	implementation(libs.dagger.hiltCore)
	implementation(libs.dagger.hiltAndroid)
	implementation(libs.inject)
	implementation(libs.kotlinResult.result)
	implementation(libs.kotlinx.collectionsImmutableJvm)
	implementation(libs.kotlinx.coroutinesCore)
	implementation(libs.kotlinx.serializationCore)
	implementation(libs.okhttp3)
	implementation(libs.retrofit)
	implementation(libs.timber)

	debugRuntimeOnly(platform(libs.androidx.compose.bom))
	// Needed for createComposeRule, NOT ONLY for createAndroidComposeRule, as in the docs
	debugRuntimeOnly(libs.androidx.composeUiTestManifest)

	ksp(libs.dagger.hiltCompiler)

	// Might be removed later when this is fixed: https://github.com/google/dagger/issues/5001
	ksp(libs.kotlin.metadataJvm)

	testImplementation(libs.mockk.core)
	testImplementation(libs.kotlinx.coroutinesTest)

	kspTest(libs.androidx.hiltCompiler)
	kspTest(libs.dagger.compiler)
	kspTest(libs.dagger.hiltCompiler)

	androidTestCompileOnly(libs.autobind.android.testing)

	androidTestImplementation(testFixtures(project(":feature:account:domain")))
	androidTestImplementation(testFixtures(project(":feature:movies:domain")))
	androidTestImplementation(testFixtures(project(":feature:tv:domain")))
	androidTestImplementation(testFixtures(project(":shared:domain")))
	androidTestImplementation(testFixtures(project(":shared:ui")))
	androidTestImplementation(libs.androidx.fragmentKtx) // transitive
	androidTestImplementation(libs.androidx.testCoreKtx)
	androidTestImplementation(libs.androidx.testExtJUnit)
	androidTestImplementation(libs.androidx.testRunner)
	androidTestImplementation(libs.androidx.composeUiTestAndroid)
	androidTestImplementation(libs.androidx.composeUiTestJunit4)
	androidTestImplementation(libs.androidx.composeUiTestJunit4Android)
	androidTestImplementation(libs.autobind.android.api)
	androidTestImplementation(libs.autobind.core) // transitive
	androidTestImplementation(libs.dagger.hiltAndroidTesting)
	androidTestImplementation(libs.jUnit)
	androidTestImplementation(libs.mockk.android)
	androidTestImplementation(libs.mockk.core)

	kspAndroidTest(libs.dagger.hiltCompiler)
	kspAndroidTest(libs.dagger.compiler)
	kspAndroidTest(libs.autobind.compiler)

	screenshotTestImplementation(libs.androidx.composeUiTooling)
	screenshotTestImplementation(libs.android.screenshotValidationApi)

}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	compilerOptions.freeCompilerArgs.addAll(
		"-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
		"-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
		"-Xjspecify-annotations=strict", // https://developer.android.com/jetpack/androidx/releases/core#1.16.0
		"-Xtype-enhancement-improvements-strict-mode",
	)
}
