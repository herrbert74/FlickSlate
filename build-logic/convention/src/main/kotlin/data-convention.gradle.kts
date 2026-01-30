plugins {
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.ksp)
}

dependencies {
	implementation(project(":base:kotlin"))
	implementation(project(":shared:domain"))

	if (project.parent?.name == "movies") {
		api(project(":shared:data"))
	} else {
		implementation(project(":shared:data"))
	}

	api(libs.dagger.hiltCore)
	implementation(libs.dagger.hiltAndroid)
	api(libs.retrofit)

	excludeFrom("account") {
		implementation(libs.androidx.roomCommon)
	}
	implementation(libs.androidx.roomRuntime)
	excludeFrom("account") {
		implementation(libs.androidx.sqlite)
	}
	implementation(libs.autobind.android.api)
	implementation(libs.autobind.core) // transitive
	implementation(libs.kotlinResult.result)
	excludeFrom("account") {
		implementation(libs.kotlinRetry)
	}
	excludeFrom("account") {
		implementation(libs.kotlinx.collectionsImmutableJvm)
		implementation(libs.kotlinx.coroutinesCore)
	}
	implementation(libs.kotlinx.serializationCore)
	implementation(libs.okhttp3)
	implementation(libs.timber)

	ksp(libs.androidx.roomCompiler)
	ksp(libs.autobind.compiler)

	testImplementation(libs.jUnit)
	testImplementation(libs.mockk.core)
	testImplementation(libs.mockk.dsl) // transitive
	testImplementation(libs.kotlinx.coroutinesTest)
	testImplementation(libs.kotlinx.serializationJson)
}

fun excludeFrom(excluded: String, dependency: () -> Unit) {
	if (!(project.parent?.name.equals(excluded))) {
		dependency()
	}
}
