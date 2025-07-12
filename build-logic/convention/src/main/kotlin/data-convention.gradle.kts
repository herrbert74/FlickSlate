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
	implementation(libs.androidx.roomCommon)
	implementation(libs.androidx.roomRuntime)
	implementation(libs.androidx.sqlite)
	implementation(libs.autobind.android.api)
	api(libs.dagger.core)
	implementation(libs.dagger.hiltCore)
	api(libs.dagger.hiltAndroid)
	api(libs.inject)
	implementation(libs.kotlinx.coroutinesCore)
	implementation(libs.kotlinx.serializationCore)
	implementation(libs.kotlinResult.result)
	implementation(libs.kotlinRetry)
	implementation(libs.kotlinx.collectionsImmutableJvm)
	api(libs.retrofit)
	implementation(libs.okhttp3)
	implementation(libs.timber)

	ksp(libs.androidx.hiltCompiler)
	ksp(libs.androidx.roomCompiler)
	ksp(libs.autobind.compiler)
	ksp(libs.dagger.compiler)
	ksp(libs.dagger.hiltCompiler)

	testImplementation(libs.jUnit)
	testImplementation(libs.kotest.assertionsShared)
	testImplementation(libs.kotest.assertionsCore)
	testImplementation(libs.mockk.core)
	testImplementation(libs.kotlinx.coroutinesTest)
	testImplementation(libs.kotlinx.serializationJson)
	testImplementation(libs.retrofit.converterKotlinxSerialization)
}
