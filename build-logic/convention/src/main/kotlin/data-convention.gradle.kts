plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.ksp)
}

ksp {
	arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {
	api(project(":shared"))
	api(project(":shared-data"))
	implementation(libs.androidx.room.common)
	implementation(libs.androidx.room.runtime)
	implementation(libs.androidx.sqlite)
	implementation(libs.autobind.android.api)
	api(libs.google.dagger.core)
	implementation(libs.google.dagger.hilt.core)
	api(libs.google.dagger.hilt.android)
	api(libs.inject)
	implementation(libs.kotlinx.coroutines.core)
	implementation(libs.kotlinx.serialization.core)
	implementation(libs.kotlinResult.result)
	implementation(libs.kotlinRetry)
	implementation(libs.kotlinx.collections.immutable.jvm)
	api(libs.squareUp.retrofit2.retrofit)
	implementation(libs.squareUp.okhttp3.okhttp)
	implementation(libs.timber)

	ksp(libs.androidx.hilt.compiler)
	ksp(libs.androidx.room.compiler)
	ksp(libs.autobind.compiler)
	ksp(libs.google.dagger.compiler)
	ksp(libs.google.dagger.hilt.compiler)

	testImplementation(libs.junit)
	testImplementation(libs.test.kotest.assertions.shared)
	testImplementation(libs.test.kotest.assertions.core)
	testImplementation(libs.test.mockk.core)
	testImplementation(libs.kotlinx.coroutines.test)
	testImplementation(libs.kotlinx.serialization.json)
	testImplementation(libs.squareUp.retrofit2.converterKotlinxSerialization)
}
