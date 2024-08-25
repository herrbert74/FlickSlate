package com.zsoltbertalan.flickslate.shared.compose.util

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

fun extractColorsFromBitmap(bitmap: Bitmap, isDarkMode: Boolean): Map<String, String> {
	val palette = Palette.from(bitmap).generate()

	return mapOf(
		"vibrant" to parseColorSwatch(
			color = if (isDarkMode) palette.darkVibrantSwatch else palette.lightVibrantSwatch
		),
		"dark" to parseColorSwatch(
			color = if (isDarkMode) palette.darkMutedSwatch else palette.lightMutedSwatch
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
