plugins {
	alias(libs.plugins.androidLibrary)
}

dependencies {
	api(libs.dagger.core)
	val androidBase = project.name == "android" && project.parent?.name == "base"
	val dataShared = project.name == "data" && project.parent?.name == "shared"
	if (androidBase || dataShared) {
		implementation(libs.inject)
	} else {
		api(libs.inject)
	}
}
