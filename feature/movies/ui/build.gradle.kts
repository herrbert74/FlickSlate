plugins {
	id("android-library-convention")
	alias(libs.plugins.kotlin.composeCompiler)
	id("coil-convention")
	id("metro-convention")
	id("ui-convention")
}

android {
	namespace = "com.zsoltbertalan.flickslate.movies.ui"

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}

	testOptions {
		screenshotTests.create("screenshotTest") {
			engineVersion = libs.versions.compose.screenshotTesting.get()
			targetVariants.add("debug")
			dependencies {
				implementation(libs.androidx.composeUiTooling)
				implementation(libs.android.screenshotValidationApi)
			}
		}
	}
}

dependencies {
	api(project(":feature:account:domain"))
	api(project(":feature:movies:domain"))

	api(libs.androidx.lifecycleCommon)
	api(libs.androidx.lifecycleViewmodelCompose)

	implementation(libs.androidx.composeUiToolingPreview) // transitive
	implementation(libs.androidx.coreKtx) // transitive
	implementation(libs.androidx.lifecycleRuntimeCompose)
	implementation(libs.androidx.composeMaterialIconsExtended)

	testImplementation(testFixtures(project(":shared:domain")))
	testImplementation(testFixtures(project(":feature:account:domain")))

	testImplementation(libs.kotest.assertionsCore)
	testImplementation(libs.mockk.dsl) // transitive
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	compilerOptions.freeCompilerArgs.add("-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi")
}
