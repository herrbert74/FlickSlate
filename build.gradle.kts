plugins {
	alias(libs.plugins.androidApplication) apply false
	alias(libs.plugins.androidLibrary) apply false
	alias(libs.plugins.kotlin.composeCompiler) apply false
	alias(libs.plugins.dependencyAnalysis)
	alias(libs.plugins.kotlin.android) apply false
	alias(libs.plugins.kotlin.jvm) apply false
	alias(libs.plugins.dagger.hiltAndroid) apply false
	alias(libs.plugins.ksp)
	alias(libs.plugins.binaryCompatibilityValidator)
	id("detekt-convention")
	id("profiler")
}

dependencyAnalysis {
	structure {
		ignoreKtx(true)
	}
	issues {
		all {
			onAny {
				exclude(
					"io.mockk:mockk-android",
					"com.jakewharton.timber:timber",
					"se.ansman.dagger.auto:android-api"
				)
			}
		}
	}
}
