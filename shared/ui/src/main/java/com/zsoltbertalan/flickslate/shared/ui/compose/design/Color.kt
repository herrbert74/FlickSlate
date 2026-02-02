@file:Suppress("unused", "MagicNumber")

package com.zsoltbertalan.flickslate.shared.ui.compose.design

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

//region Normal colors

val primaryLight = Color(0xFF006D42)
val onPrimaryLight = Color(0xFFFFFFFF)
val primaryContainerLight = Color(0xFF4CCD8B)
val onPrimaryContainerLight = Color(0xFF00311C)
val secondaryLight = Color(0xFF00677C)
val onSecondaryLight = Color(0xFFFFFFFF)
val secondaryContainerLight = Color(0xFF27BBDD)
val onSecondaryContainerLight = Color(0xFF00252E)
val tertiaryLight = Color(0xFFA63B00)
val onTertiaryLight = Color(0xFFFFFFFF)
val tertiaryContainerLight = Color(0xFFFF7132)
val onTertiaryContainerLight = Color(0xFF200500)
val errorLight = Color(0xFFBA1A1A)
val onErrorLight = Color(0xFFFFFFFF)
val errorContainerLight = Color(0xFFFFDAD6)
val onErrorContainerLight = Color(0xFF410002)
val backgroundLight = Color(0xFFF4FBF3)
val onBackgroundLight = Color(0xFF171D18)
val surfaceLight = Color(0xFFF4FBF3)
val onSurfaceLight = Color(0xFF171D18)
val surfaceVariantLight = Color(0xFFD8E6DA)
val onSurfaceVariantLight = Color(0xFF3D4A41)
val outlineLight = Color(0xFF6D7A70)
val outlineVariantLight = Color(0xFFBCCABE)
val scrimLight = Color(0xFF000000)
val inverseSurfaceLight = Color(0xFF2B322D)
val inverseOnSurfaceLight = Color(0xFFECF3EB)
val inversePrimaryLight = Color(0xFF5EDD9B)
val surfaceDimLight = Color(0xFFD5DCD4)
val surfaceBrightLight = Color(0xFFF4FBF3)
val surfaceContainerLowestLight = Color(0xFFFFFFFF)
val surfaceContainerLowLight = Color(0xFFEFF5ED)
val surfaceContainerLight = Color(0xFFE9F0E8)
val surfaceContainerHighLight = Color(0xFFE3EAE2)
val surfaceContainerHighestLight = Color(0xFFDDE4DC)

val primaryDark = Color(0xFF65E4A0)
val onPrimaryDark = Color(0xFF003920)
val primaryContainerDark = Color(0xFF34B97A)
val onPrimaryContainerDark = Color(0xFF001D0E)
val secondaryDark = Color(0xFF51D6F9)
val onSecondaryDark = Color(0xFF003642)
val secondaryContainerDark = Color(0xFF00A6C7)
val onSecondaryContainerDark = Color(0xFF00060A)
val tertiaryDark = Color(0xFFFFB599)
val onTertiaryDark = Color(0xFF5A1C00)
val tertiaryContainerDark = Color(0xFFCB4900)
val onTertiaryContainerDark = Color(0xFFFFFFFF)
val errorDark = Color(0xFFFFB4AB)
val onErrorDark = Color(0xFF690005)
val errorContainerDark = Color(0xFF93000A)
val onErrorContainerDark = Color(0xFFFFDAD6)
val backgroundDark = Color(0xFF0E1510)
val onBackgroundDark = Color(0xFFDDE4DC)
val surfaceDark = Color(0xFF0E1510)
val onSurfaceDark = Color(0xFFDDE4DC)
val surfaceVariantDark = Color(0xFF3D4A41)
val onSurfaceVariantDark = Color(0xFFBCCABE)
val outlineDark = Color(0xFF879489)
val outlineVariantDark = Color(0xFF3D4A41)
val scrimDark = Color(0xFF000000)
val inverseSurfaceDark = Color(0xFFDDE4DC)
val inverseOnSurfaceDark = Color(0xFF2B322D)
val inversePrimaryDark = Color(0xFF006D42)
val surfaceDimDark = Color(0xFF0E1510)
val surfaceBrightDark = Color(0xFF343B35)
val surfaceContainerLowestDark = Color(0xFF09100B)
val surfaceContainerLowDark = Color(0xFF171D18)
val surfaceContainerDark = Color(0xFF1B211C)
val surfaceContainerHighDark = Color(0xFF252C27)
val surfaceContainerHighestDark = Color(0xFF303631)

//endregion

//region Additional colours

@Immutable
data class AdditionalColorScheme(

	val quaternaryContainer: Color = Color.Unspecified,
	val quinaryContainer: Color = Color.Unspecified,
	val senaryContainer: Color = Color.Unspecified

)

val quaternaryLight = Color(0xFF8E4F00)
val onQuaternaryLight = Color(0xFFFFFFFF)
val quaternaryContainerLight = Color(0xFFFFB067)
val onQuaternaryContainerLight = Color(0xFF4E2900)
val quinaryLight = Color(0xFF6C5E00)
val onQuinaryLight = Color(0xFFFFFFFF)
val quinaryContainerLight = Color(0xFFF0D313)
val onQuinaryContainerLight = Color(0xFF483E00)
val senaryLight = Color(0xFF963A87)
val onSenaryLight = Color(0xFFFFFFFF)
val senaryContainerLight = Color(0xFFEF86D9)
val onSenaryContainerLight = Color(0xFF41003A)

val quaternaryDark = Color(0xFFFFD5B2)
val onQuaternaryDark = Color(0xFF4C2700)
val quaternaryContainerDark = Color(0xFFF69F4B)
val onQuaternaryContainerDark = Color(0xFF3F2000)
val quinaryDark = Color(0xFFFFF2BB)
val onQuinaryDark = Color(0xFF383000)
val quinaryContainerDark = Color(0xFFE3C700)
val onQuinaryContainerDark = Color(0xFF403700)
val senaryDark = Color(0xFFFFACEA)
val onSenaryDark = Color(0xFF5D0055)
val senaryContainerDark = Color(0xFFDB75C7)
val onSenaryContainerDark = Color(0xFF170014)

val LightAdditionalColorScheme = AdditionalColorScheme(
	quaternaryContainer = quaternaryContainerLight,
	quinaryContainer = quinaryContainerLight,
	senaryContainer = senaryContainerLight
)

val DarkAdditionalColorScheme = AdditionalColorScheme(
	quaternaryContainer = quaternaryContainerDark,
	quinaryContainer = quinaryContainerDark,
	senaryContainer = senaryContainerDark
)

val LocalAdditionalColors = staticCompositionLocalOf { AdditionalColorScheme() }

//endregion

//region Fixed colours

data class FixedColorScheme(
	val primaryFixed: Color,
	val onPrimaryFixed: Color,
	val secondaryFixed: Color,
	val onSecondaryFixed: Color,
	val tertiaryFixed: Color,
	val onTertiaryFixed: Color,
	val quinaryFixed: Color,
	val onQuinaryFixed: Color,
	val primaryFixedDim: Color,
	val secondaryFixedDim: Color,
	val tertiaryFixedDim: Color,
	val inverseSurfaceFixed: Color,
	val inverseOnSurfaceFixed: Color,
)

fun getFixedColors() = FixedColorScheme(
	primaryFixed = primaryContainerLight,
	onPrimaryFixed = onPrimaryContainerLight,
	secondaryFixed = secondaryContainerLight,
	onSecondaryFixed = onSecondaryContainerLight,
	tertiaryFixed = tertiaryLight,
	onTertiaryFixed = onTertiaryContainerLight,
	quinaryFixed = quinaryContainerLight,
	onQuinaryFixed = onQuinaryContainerLight,
	primaryFixedDim = primaryDark,
	secondaryFixedDim = secondaryDark,
	tertiaryFixedDim = tertiaryDark,
	inverseSurfaceFixed = inverseSurfaceLight,
	inverseOnSurfaceFixed = inverseOnSurfaceLight,
)

val LocalFixedColors = staticCompositionLocalOf { getFixedColors() }

//endregion
