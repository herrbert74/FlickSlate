import com.zsoltbertalan.flickslate.convention.libs

plugins {
	id("com.google.devtools.ksp")
}

ksp {
	arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {
	"api"(project(":shared"))
	"api"(project(":shared-data"))
	"implementation"(libs.findLibrary("androidx.room.common").get())
	"implementation"(libs.findLibrary("androidx.room.runtime").get())
	"implementation"(libs.findLibrary("androidx.sqlite").get())
	"implementation"(libs.findLibrary("autobind.android.api").get())
	"api"(libs.findLibrary("google.dagger.core").get())
	"implementation"(libs.findLibrary("google.dagger.hilt.core").get())
	"api"(libs.findLibrary("google.dagger.hilt.android").get())
	"api"(libs.findLibrary("inject").get())
	"implementation"(libs.findLibrary("kotlinx.coroutines.core").get())
	"implementation"(libs.findLibrary("kotlinx.serialization.core").get())
	"implementation"(libs.findLibrary("kotlinResult.result").get())
	"implementation"(libs.findLibrary("kotlinx.collections.immutable.jvm").get())
	"api"(libs.findLibrary("squareUp.retrofit2.retrofit").get())
	"implementation"(libs.findLibrary("squareUp.okhttp3.okhttp").get())
	"implementation"(libs.findLibrary("timber").get())

	"ksp"(libs.findLibrary("androidx.hilt.compiler").get())
	"ksp"(libs.findLibrary("androidx.room.compiler").get())
	"ksp"(libs.findLibrary("autobind.compiler").get())
	"ksp"(libs.findLibrary("google.dagger.compiler").get())
	"ksp"(libs.findLibrary("google.dagger.hilt.compiler").get())

	"testImplementation"(libs.findLibrary("junit").get())
	"testImplementation"(libs.findLibrary("test.kotest.assertions.shared").get())
	"testImplementation"(libs.findLibrary("test.kotest.assertions.core").get())
	"testImplementation"(libs.findLibrary("test.mockk.core").get())
	"testImplementation"(libs.findLibrary("kotlinx.coroutines.test").get())
	"testImplementation"(libs.findLibrary("kotlinx.serialization.json").get())
	"testImplementation"(libs.findLibrary("squareUp.retrofit2.converterKotlinxSerialization").get())
}
