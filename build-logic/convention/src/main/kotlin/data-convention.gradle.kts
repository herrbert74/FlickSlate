plugins {
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.ksp)
}

ksp {
	arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {
	implementation(project(":base:kotlin"))
	implementation(project(":shared:domain"))
	implementation(project(":shared:data"))

	api(libs.dagger.hiltCore)
	implementation(libs.dagger.hiltAndroid)
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

	ksp(libs.androidx.roomCompiler)
	ksp(libs.autobind.compiler)

	testImplementation(libs.jUnit)
	testImplementation(libs.mockk.core)
	testImplementation(libs.mockk.dsl) // transitive
	testImplementation(libs.kotlinx.coroutinesTest)
	testImplementation(libs.kotlinx.serializationJson)
}
