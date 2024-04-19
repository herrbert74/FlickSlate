package com.zsoltbertalan.flickslate.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.palette.graphics.Palette
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult

suspend fun convertImageUrlToBitmap(
	imageUrl: String,
	context: Context
): Bitmap? {
	val loader = ImageLoader(context = context)
	val request = ImageRequest.Builder(context = context)
		.data(imageUrl)
		.allowHardware(false)
		.build()
	val imageResult = loader.execute(request = request)
	return if (imageResult is SuccessResult) {
		(imageResult.drawable as BitmapDrawable).bitmap
	} else {
		null
	}
}

fun extractColorsFromBitmap(bitmap: Bitmap): Map<String, String> {
	return mapOf(
		"vibrant" to parseColorSwatch(
			color = Palette.from(bitmap).generate().lightVibrantSwatch
		),
		"dark" to parseColorSwatch(
			color = Palette.from(bitmap).generate().vibrantSwatch
		),
	)
}

private fun parseColorSwatch(color: Palette.Swatch?): String {
	return if (color != null) {
		val parsedColor = Integer.toHexString(color.rgb)
		return "#$parsedColor"
	} else {
		"#000000"
	}
}
