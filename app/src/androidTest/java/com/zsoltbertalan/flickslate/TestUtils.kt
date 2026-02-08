package com.zsoltbertalan.flickslate

import android.app.Application

fun Application.asTestOverrides(): TestOverrides =
	(this as TestFlickSlateApp).testOverrides
