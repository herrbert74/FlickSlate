plugins {
	alias(libs.plugins.androidApplication) apply false
	alias(libs.plugins.androidLibrary) apply false
	alias(libs.plugins.kotlin.composeCompiler) apply false
	alias(libs.plugins.detekt) apply false
	alias(libs.plugins.dependencyAnalysis)
	alias(libs.plugins.kotlin.android) apply false
	alias(libs.plugins.kotlin.jvm) apply false
	alias(libs.plugins.dagger.hiltAndroid) apply false
	alias(libs.plugins.ksp)
}

dependencyAnalysis {
	structure {
		ignoreKtx(true)
	}
	issues {
		all {
			onAny {
				exclude(
					//"org.jetbrains.kotlin:kotlin-stdlib", //This might be a bug from the plugin
					"io.mockk:mockk-android",
					"com.jakewharton.timber:timber",
					"se.ansman.dagger.auto:android-api"
				)
			}
		}
	}
}
