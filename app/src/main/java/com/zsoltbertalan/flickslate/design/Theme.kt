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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext

val FlickSlateLightColorScheme = lightColorScheme(
	primary = primaryLight,
	onPrimary = onPrimaryLight,
	primaryContainer = primaryContainerLight,
	onPrimaryContainer = onPrimaryContainerLight,
	secondary = secondaryLight,
	onSecondary = onSecondaryLight,
	secondaryContainer = secondaryContainerLight,
	onSecondaryContainer = onSecondaryContainerLight,
	tertiary = tertiaryLight,
	onTertiary = onTertiaryLight,
	tertiaryContainer = tertiaryContainerLight,
	onTertiaryContainer = onTertiaryContainerLight,
	error = errorLight,
	errorContainer = errorContainerLight,
	onError = onErrorLight,
	onErrorContainer = onErrorContainerLight,
	background = backgroundLight,
	onBackground = onBackgroundLight,
	surface = surfaceLight,
	onSurface = onSurfaceLight,
	surfaceVariant = surfaceVariantLight,
	onSurfaceVariant = onSurfaceVariantLight,
	outline = outlineLight,
	outlineVariant = outlineVariantLight,
	inverseSurface = inverseSurfaceLight,
	inverseOnSurface = inverseOnSurfaceLight,
	inversePrimary = inversePrimaryLight,
	scrim = scrimLight,
	surfaceBright = surfaceBrightLight,
	surfaceDim = surfaceDimLight,
	surfaceContainerLowest = surfaceContainerLowestLight,
	surfaceContainerLow = surfaceContainerLowLight,
	surfaceContainer = surfaceContainerLight,
	surfaceContainerHigh = surfaceContainerHighLight,
	surfaceContainerHighest = surfaceContainerHighestLight,
)

val FlickSlateDarkColorScheme = darkColorScheme(
	primary = primaryDark,
	onPrimary = onPrimaryDark,
	primaryContainer = primaryContainerDark,
	onPrimaryContainer = onPrimaryContainerDark,
	secondary = secondaryDark,
	onSecondary = onSecondaryDark,
	secondaryContainer = secondaryContainerDark,
	onSecondaryContainer = onSecondaryContainerDark,
	tertiary = tertiaryDark,
	onTertiary = onTertiaryDark,
	tertiaryContainer = tertiaryContainerDark,
	onTertiaryContainer = onTertiaryContainerDark,
	error = errorDark,
	errorContainer = errorContainerDark,
	onError = onErrorDark,
	onErrorContainer = onErrorContainerDark,
	background = backgroundDark,
	onBackground = onBackgroundDark,
	surface = surfaceDark,
	onSurface = onSurfaceDark,
	surfaceVariant = surfaceVariantDark,
	onSurfaceVariant = onSurfaceVariantDark,
	outline = outlineDark,
	outlineVariant = outlineVariantDark,
	inverseSurface = inverseSurfaceDark,
	inverseOnSurface = inverseOnSurfaceDark,
	inversePrimary = inversePrimaryDark,
	scrim = scrimDark,
	surfaceBright = surfaceBrightDark,
	surfaceDim = surfaceDimDark,
	surfaceContainerLowest = surfaceContainerLowestDark,
	surfaceContainerLow = surfaceContainerLowDark,
	surfaceContainer = surfaceContainerDark,
	surfaceContainerHigh = surfaceContainerHighDark,
	surfaceContainerHighest = surfaceContainerHighestDark,
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
	additionalColorScheme: AdditionalColorScheme,
	fixedColorScheme: FixedColorScheme = LocalFixedColors.current,
	content: @Composable () -> Unit,
) {
	val colorCache = remember { colorScheme }
	val customColorCache = remember { additionalColorScheme }
	val fixedColorCache = remember { fixedColorScheme }

	CompositionLocalProvider(
		LocalAppColors provides colorCache,
		LocalAdditionalColors provides customColorCache,
		LocalFixedColors provides fixedColorCache,
		content = content
	)
}

val LocalAppColors = staticCompositionLocalOf {
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

	// logic for which custom palette to use
	val customColorsPalette =
		if (isDarkTheme) DarkAdditionalColorScheme
		else LightAdditionalColorScheme

	val configuration = LocalConfiguration.current
	val dimensions = if (configuration.smallestScreenWidthDp <= SMALLEST_WIDTH_600) smallDimensions else sw600Dimensions

	ProvideDimens(dimensions = dimensions) {
		ProvideColors(colorScheme = colorScheme, additionalColorScheme = customColorsPalette) {
			MaterialTheme(
				colorScheme = colorScheme,
				typography = FlickSlateTypography,
				content = content
			)
		}
	}

}

val Dimens: Dimensions
	@Composable
	get() = LocalAppDimens.current

val Colors: ColorScheme
	@Composable
	get() = LocalAppColors.current

val AdditionalColors: AdditionalColorScheme
	@Composable
	get() = LocalAdditionalColors.current

val FixedColors: FixedColorScheme
	@Composable
	get() = LocalFixedColors.current
