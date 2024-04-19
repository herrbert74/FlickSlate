plugins {
	alias(libs.plugins.android.application) apply false
	alias(libs.plugins.android.kotlin) apply false
	alias(libs.plugins.ksp)
	alias(libs.plugins.realm) apply false
	alias(libs.plugins.google.dagger.hilt.android) apply false
}
