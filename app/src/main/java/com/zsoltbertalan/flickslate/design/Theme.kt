package com.zsoltbertalan.flickslate.design

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext

val FlickSlateLightColorScheme = lightColorScheme(
	primary = Color(LIGHT_PRIMARY),
	onPrimary = Color(LIGHT_ON_PRIMARY),
	primaryContainer = Color(LIGHT_PRIMARY_CONTAINER),
	onPrimaryContainer = Color(LIGHT_ON_PRIMARY_CONTAINER),
	secondary = Color(LIGHT_SECONDARY),
	onSecondary = Color(LIGHT_ON_SECONDARY),
	secondaryContainer = Color(LIGHT_SECONDARY_CONTAINER),
	onSecondaryContainer = Color(LIGHT_ON_SECONDARY_CONTAINER),
	tertiary = Color(LIGHT_TERTIARY),
	onTertiary = Color(LIGHT_ON_TERTIARY),
	tertiaryContainer = Color(LIGHT_TERTIARY_CONTAINER),
	onTertiaryContainer = Color(LIGHT_ON_TERTIARY_CONTAINER),
	error = Color(LIGHT_ERROR),
	errorContainer = Color(LIGHT_ERROR_CONTAINER),
	onError = Color(LIGHT_ON_ERROR),
	onErrorContainer = Color(LIGHT_ON_ERROR_CONTAINER),
	background = Color(LIGHT_BACKGROUND),
	onBackground = Color(LIGHT_ON_BACKGROUND),
	surface = Color(LIGHT_SURFACE),
	onSurface = Color(LIGHT_ON_SURFACE),
	surfaceVariant = Color(LIGHT_SURFACE_VARIANT),
	onSurfaceVariant = Color(LIGHT_ON_SURFACE_VARIANT),
	outline = Color(LIGHT_OUTLINE),
	inverseOnSurface = Color(LIGHT_INVERSE_ON_SURFACE),
	inverseSurface = Color(LIGHT_INVERSE_SURFACE),
	inversePrimary = Color(LIGHT_INVERSE_PRIMARY),
	outlineVariant = Color(LIGHT_OUTLINE_VARIANT),
	scrim = Color(LIGHT_SCRIM),
	surfaceTint = Color(LIGHT_SURFACE_TINT)
)
private val FlickSlateDarkColorScheme = darkColorScheme(

	primary = Color(DARK_PRIMARY),
	onPrimary = Color(DARK_ON_PRIMARY),
	primaryContainer = Color(DARK_PRIMARY_CONTAINER),
	onPrimaryContainer = Color(DARK_ON_PRIMARY_CONTAINER),
	secondary = Color(DARK_SECONDARY),
	onSecondary = Color(DARK_ON_SECONDARY),
	secondaryContainer = Color(DARK_SECONDARY_CONTAINER),
	onSecondaryContainer = Color(DARK_ON_SECONDARY_CONTAINER),
	tertiary = Color(DARK_TERTIARY),
	onTertiary = Color(DARK_ON_TERTIARY),
	tertiaryContainer = Color(DARK_TERTIARY_CONTAINER),
	onTertiaryContainer = Color(DARK_ON_TERTIARY_CONTAINER),
	error = Color(DARK_ERROR),
	errorContainer = Color(DARK_ERROR_CONTAINER),
	onError = Color(DARK_ON_ERROR),
	onErrorContainer = Color(DARK_ON_ERROR_CONTAINER),
	background = Color(DARK_BACKGROUND),
	onBackground = Color(DARK_ON_BACKGROUND),
	surface = Color(DARK_SURFACE),
	onSurface = Color(DARK_ON_SURFACE),
	surfaceVariant = Color(DARK_SURFACE_VARIANT),
	onSurfaceVariant = Color(DARK_ON_SURFACE_VARIANT),
	outline = Color(DARK_OUTLINE),
	inverseOnSurface = Color(DARK_INVERSE_ON_SURFACE),
	inverseSurface = Color(DARK_INVERSE_SURFACE),
	inversePrimary = Color(DARK_INVERSE_PRIMARY),
	outlineVariant = Color(DARK_OUTLINE_VARIANT),
	scrim = Color(DARK_SCRIM),
	surfaceTint = Color(DARK_SURFACE_TINT)
)

@Composable
fun ProvideDimens(
	dimensions: Dimensions,
	content: @Composable () -> Unit,
) {
	val dimensionSet = remember { dimensions }
	CompositionLocalProvider(LocalAppDimens provides dimensionSet, content = content)
}

private val LocalAppDimens = staticCompositionLocalOf {
	smallDimensions
}

@Composable
fun ProvideColors(
	colorScheme: ColorScheme,
	content: @Composable () -> Unit,
) {
	val colorPalette = remember { colorScheme }
	CompositionLocalProvider(LocalAppColors provides colorPalette, content = content)
}

private val LocalAppColors = staticCompositionLocalOf {
	FlickSlateLightColorScheme
}

@Composable
fun FlickSlateTheme(
	isDarkTheme: Boolean = isSystemInDarkTheme(),
	isDynamicColor: Boolean = false,
	content: @Composable () -> Unit,
) {

	val dynamicColor = isDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

	val colorScheme = when {
		dynamicColor && isDarkTheme -> dynamicDarkColorScheme(LocalContext.current)
		dynamicColor && !isDarkTheme -> dynamicLightColorScheme(LocalContext.current)
		isDarkTheme -> FlickSlateDarkColorScheme
		else -> FlickSlateLightColorScheme
	}
	val configuration = LocalConfiguration.current
	val dimensions = if (configuration.smallestScreenWidthDp <= SMALLEST_WIDTH_600) smallDimensions else sw600Dimensions

	ProvideDimens(dimensions = dimensions) {
		ProvideColors(colorScheme = colorScheme) {
			MaterialTheme(
				colorScheme = colorScheme,
				typography = FlickSlateTypography,
				content = content
			)
		}
	}

}

object FlickSlateTheme {
	val colorScheme: ColorScheme
		@Composable
		get() = LocalAppColors.current

	val dimens: Dimensions
		@Composable
		get() = LocalAppDimens.current
}

val Dimens: Dimensions
	@Composable
	get() = FlickSlateTheme.dimens

val Colors: ColorScheme
	@Composable
	get() = FlickSlateTheme.colorScheme
