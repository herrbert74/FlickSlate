plugins {
	alias(libs.plugins.android.application) apply false
	alias(libs.plugins.android.kotlin) apply false
	alias(libs.plugins.compose.compiler) apply false
	alias(libs.plugins.ksp)
//	alias(libs.plugins.realm) apply false
	alias(libs.plugins.google.dagger.hilt.android) apply false
}

buildscript {
	dependencies {
		classpath("io.realm.kotlin:gradle-plugin:2.0.0-SNAPSHOT")
	}
}
