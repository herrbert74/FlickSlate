import com.zsoltbertalan.flickslate.convention.commonConfiguration
import com.zsoltbertalan.flickslate.convention.configureKotlinAndroid
import com.zsoltbertalan.flickslate.convention.libs

plugins {
	id("com.android.library")
	id("io.gitlab.arturbosch.detekt")
	id("com.autonomousapps.dependency-analysis")
	kotlin("android")
}

apply(from = project.rootProject.file("config/detekt/detekt.gradle"))

android {
	commonConfiguration(this)
}

kotlin {
	configureKotlinAndroid(this)
}

dependencies {
	"detektPlugins"(libs.findLibrary("detekt.compose").get())
}
