import com.zsoltbertalan.flickslate.convention.composeConfiguration
import com.zsoltbertalan.flickslate.convention.libs

plugins {
	id("com.android.library")
	kotlin("android")
}

android {
	composeConfiguration(this)
}

dependencies {
	"implementation"(project(":shared"))

	"implementation"(platform(libs.findLibrary("androidx.compose.bom").get()))

	"implementation"(libs.findLibrary("androidx.compose.foundation").get())
	"implementation"(libs.findLibrary("androidx.compose.ui.ui").get())
	"implementation"(libs.findLibrary("androidx.compose.ui.graphics").get())
	"implementation"(libs.findLibrary("androidx.compose.ui.text").get())
	"implementation"(libs.findLibrary("androidx.compose.ui.unit").get())
	"implementation"(libs.findLibrary("androidx.compose.ui.tooling").get())
	"implementation"(libs.findLibrary("androidx.compose.ui.toolingPreview").get())
	"implementation"(libs.findLibrary("androidx.compose.material3").get())
	"implementation"(libs.findLibrary("androidx.coreKtx").get())
	"implementation"(libs.findLibrary("androidx.hilt.navigation.compose").get())
	"implementation"(libs.findLibrary("androidx.lifecycle.runtime").get())
	"implementation"(libs.findLibrary("androidx.lifecycle.runtime.compose").get())
	"implementation"(libs.findLibrary("androidx.navigation.common").get())
	"implementation"(libs.findLibrary("androidx.navigation.compose").get())
	"implementation"(libs.findLibrary("coil").get())
	"implementation"(libs.findLibrary("google.dagger.core").get())
	"implementation"(libs.findLibrary("google.dagger.hilt.android").get())
	"implementation"(libs.findLibrary("google.material").get())
	"implementation"(libs.findLibrary("kotlinResult.result").get())
	"implementation"(libs.findLibrary("kotlinResult.coroutines").get())
	"implementation"(libs.findLibrary("timber").get())

	"ksp"(libs.findLibrary("androidx.hilt.compiler").get())
	"ksp"(libs.findLibrary("androidx.room.compiler").get())
	"ksp"(libs.findLibrary("google.dagger.compiler").get())
	"ksp"(libs.findLibrary("google.dagger.hilt.compiler").get())
	"ksp"(libs.findLibrary("google.dagger.hilt.androidCompiler").get())

	"testImplementation"(libs.findLibrary("junit").get())
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	compilerOptions.freeCompilerArgs.add("-opt-in=androidx.compose.material3.ExperimentalMaterial3Api")
}
