plugins {
	alias(libs.plugins.android.application) apply false
	alias(libs.plugins.android.library) apply false
	alias(libs.plugins.compose.compiler) apply false
	alias(libs.plugins.detekt) apply false
	alias(libs.plugins.dependency.analysis)
	alias(libs.plugins.jetbrains.kotlin.android) apply false
	alias(libs.plugins.jetbrains.kotlin.jvm) apply false
	alias(libs.plugins.google.dagger.hilt.android) apply false
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
				)
			}
		}
	}
}
