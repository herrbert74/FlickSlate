plugins {
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.ksp)
}

dependencies {
	api(libs.dagger.core)
	// api(libs.dagger.hiltAndroid) // data version
	// api(libs.dagger.hiltCore) // data version
	val androidBase = project.name == "android" && project.parent?.name == "base"
	val dataShared = project.name == "data" && project.parent?.name == "shared"
	if (androidBase || dataShared) {
		implementation(libs.inject)
	} else {
		api(libs.inject)
	}

	// implementation(libs.autobind.android.api) // Only in data modules
	// implementation(libs.autobind.core) // Only in data modules
	// implementation(libs.dagger.hiltCore) // ui version
	// implementation(libs.dagger.hiltAndroid) // ui version

	ksp(libs.androidx.hiltCompiler)
	// ksp(libs.autobind.compiler) // Only in data modules
	ksp(libs.dagger.compiler)
	ksp(libs.dagger.hiltCompiler)
}
