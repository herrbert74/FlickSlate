plugins {
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.ksp)
}

ksp {
	arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {
	api(project(":shared"))
	api(project(":shared-data"))

	api(libs.dagger.core)
	api(libs.dagger.hiltCore)
	api(libs.dagger.hiltAndroid)
	api(libs.inject)
	api(libs.retrofit)

	implementation(libs.kotlinx.coroutinesCore)
	implementation(libs.androidx.roomCommon)
	implementation(libs.androidx.roomRuntime)
	implementation(libs.androidx.sqlite)
	implementation(libs.autobind.android.api)
	implementation(libs.autobind.core) // transitive
	implementation(libs.kotlinx.serializationCore)
	implementation(libs.kotlinResult.result)
	implementation(libs.kotlinRetry)
	implementation(libs.kotlinx.collectionsImmutableJvm)
	implementation(libs.okhttp3)
	implementation(libs.timber)

	ksp(libs.androidx.hiltCompiler)
	ksp(libs.androidx.roomCompiler)
	ksp(libs.autobind.compiler)
	ksp(libs.dagger.compiler)
	ksp(libs.dagger.hiltCompiler)

	testImplementation(libs.jUnit)
	testImplementation(libs.mockk.core)
	testImplementation(libs.mockk.dsl) // transitive
	testImplementation(libs.kotlinx.coroutinesTest)
	testImplementation(libs.kotlinx.serializationJson)
}
