import com.zsoltbertalan.flickslate.convention.libs

dependencies {
	"implementation"(libs.findLibrary("baBeStudios.base.data").get())
	"implementation"(libs.findLibrary("squareUp.retrofit2.retrofit").get())
	"implementation"(libs.findLibrary("androidx.room.common").get())
	"implementation"(libs.findLibrary("androidx.room.ktx").get())
	"implementation"(libs.findLibrary("androidx.room.runtime").get())
	"ksp"(libs.findLibrary("androidx.room.compiler").get())
	"implementation"(libs.findLibrary("inject").get())
	"implementation"(libs.findLibrary("google.dagger.core").get())
	"ksp"(libs.findLibrary("google.dagger.compiler").get())

	"implementation"(libs.findLibrary("kotlinx.serialization.json").get())
	"implementation"(libs.findLibrary("google.dagger.hilt.android").get())
	"ksp"(libs.findLibrary("androidx.hilt.compiler").get())
	"ksp"(libs.findLibrary("google.dagger.hilt.compiler").get())

	"implementation"(libs.findLibrary("kotlinResult.result").get())
	"implementation"(libs.findLibrary("kotlinResult.coroutines").get())
	"implementation"(libs.findLibrary("timber").get())

	"implementation"(libs.findLibrary("androidx.coreKtx").get())
	"implementation"(libs.findLibrary("androidx.appcompat").get())
	"implementation"(libs.findLibrary("google.material").get())
	"testImplementation"(libs.findLibrary("junit").get())
}
