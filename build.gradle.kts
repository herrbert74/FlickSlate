plugins {
	alias(libs.plugins.android.application) apply false
	alias(libs.plugins.jetbrains.kotlin.android) apply false
	alias(libs.plugins.compose.compiler) apply false
	alias(libs.plugins.ksp)
	alias(libs.plugins.google.dagger.hilt.android) apply false
	alias(libs.plugins.detekt) apply false
	alias(libs.plugins.android.library) apply false
	alias(libs.plugins.jetbrains.kotlin.jvm) apply false
}
