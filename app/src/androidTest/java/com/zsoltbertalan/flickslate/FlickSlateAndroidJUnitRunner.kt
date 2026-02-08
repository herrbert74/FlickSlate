package com.zsoltbertalan.flickslate

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

@Suppress("unused")
class FlickSlateAndroidJUnitRunner : AndroidJUnitRunner() {

	override fun newApplication(classLoader: ClassLoader, className: String, context: Context): Application {
		return super.newApplication(classLoader, TestFlickSlateApp::class.java.name, context)
	}

}
