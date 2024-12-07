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
	"api"(project(":shared"))

	"implementation"(platform(libs.findLibrary("androidx.compose.bom").get()))

	"implementation"(libs.findLibrary("androidx.compose.foundation").get())
	"implementation"(libs.findLibrary("androidx.compose.foundationLayout").get())
	"implementation"(libs.findLibrary("androidx.compose.runtime.saveable").get())
	"implementation"(libs.findLibrary("androidx.compose.ui.ui").get())
	"implementation"(libs.findLibrary("androidx.compose.ui.graphics").get())
	"implementation"(libs.findLibrary("androidx.compose.ui.text").get())
	"implementation"(libs.findLibrary("androidx.compose.ui.unit").get())
	"implementation"(libs.findLibrary("androidx.compose.ui.tooling").get())
//	"implementation"(libs.findLibrary("androidx.compose.ui.toolingPreview").get())
	"implementation"(libs.findLibrary("androidx.compose.material.icons.core").get())
	"implementation"(libs.findLibrary("androidx.compose.material3").get())
	"api"(libs.findLibrary("androidx.compose.runtime").get())
	"api"(libs.findLibrary("androidx.lifecycle.viewmodel").get())
	"api"(libs.findLibrary("androidx.lifecycle.viewmodel.savedstate").get())
	"implementation"(libs.findLibrary("kotlinx.collections.immutable.jvm").get())
	"api"(libs.findLibrary("kotlinx.coroutines.core").get())
	"implementation"(libs.findLibrary("coil.compose").get())
	"implementation"(libs.findLibrary("coil.compose.base").get())
	"api"(libs.findLibrary("google.dagger.core").get())
	"implementation"(libs.findLibrary("google.dagger.hilt.android").get())
	"api"(libs.findLibrary("inject").get())
	"implementation"(libs.findLibrary("kotlinResult.result").get())
	"implementation"(libs.findLibrary("timber").get())

	"ksp"(libs.findLibrary("androidx.hilt.compiler").get())
	"ksp"(libs.findLibrary("androidx.room.compiler").get())
	"ksp"(libs.findLibrary("google.dagger.compiler").get())
	"ksp"(libs.findLibrary("google.dagger.hilt.compiler").get())
	"ksp"(libs.findLibrary("google.dagger.hilt.androidCompiler").get())

//	"testImplementation"(libs.findLibrary("junit").get())
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
	compilerOptions.freeCompilerArgs.add("-opt-in=androidx.compose.material3.ExperimentalMaterial3Api")
}
