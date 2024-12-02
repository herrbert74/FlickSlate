import com.zsoltbertalan.flickslate.convention.commonConfiguration
import com.zsoltbertalan.flickslate.convention.configureKotlinAndroid

plugins {
	id("com.android.library")
	kotlin("android")
}

android {
	commonConfiguration(this)
}

kotlin {
	configureKotlinAndroid(this)
}
