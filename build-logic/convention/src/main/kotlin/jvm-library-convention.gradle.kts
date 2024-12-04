import com.zsoltbertalan.flickslate.convention.configureKotlin

plugins {
	kotlin("jvm")
	//alias(libs.plugins.jetbrains.kotlin.jvm)
}

kotlin {
	configureKotlin(this)
}
