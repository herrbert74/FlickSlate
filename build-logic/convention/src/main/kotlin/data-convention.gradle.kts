import com.zsoltbertalan.flickslate.convention.libs

plugins {
	id("com.google.devtools.ksp")
}

ksp {
	arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {
	"implementation"(libs.findLibrary("androidx.appcompat").get())
	"implementation"(libs.findLibrary("androidx.coreKtx").get())
	"implementation"(libs.findLibrary("androidx.room.common").get())
	"implementation"(libs.findLibrary("androidx.room.ktx").get())
	"implementation"(libs.findLibrary("androidx.room.runtime").get())
	"implementation"(libs.findLibrary("baBeStudios.base.data").get())
	"implementation"(libs.findLibrary("google.dagger.core").get())
	"implementation"(libs.findLibrary("google.dagger.hilt.android").get())
	"implementation"(libs.findLibrary("google.material").get())
	"implementation"(libs.findLibrary("inject").get())
	"implementation"(libs.findLibrary("kotlinResult.result").get())
	"implementation"(libs.findLibrary("kotlinResult.coroutines").get())
	"implementation"(libs.findLibrary("kotlinx.serialization.json").get())
	"implementation"(libs.findLibrary("squareUp.retrofit2.retrofit").get())
	"implementation"(libs.findLibrary("timber").get())

	"ksp"(libs.findLibrary("androidx.hilt.compiler").get())
	"ksp"(libs.findLibrary("androidx.room.compiler").get())
	"ksp"(libs.findLibrary("google.dagger.compiler").get())
	"ksp"(libs.findLibrary("google.dagger.hilt.compiler").get())

	"testImplementation"(libs.findLibrary("junit").get())
}
