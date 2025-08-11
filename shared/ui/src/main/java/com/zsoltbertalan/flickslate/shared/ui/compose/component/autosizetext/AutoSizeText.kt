package com.zsoltbertalan.flickslate.shared.ui.compose.component.autosizetext

/*
MIT License
Copyright (c) 2024 Reda El Madini
Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.isSpecified
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastAll
import androidx.compose.ui.util.fastFilter
import com.zsoltbertalan.flickslate.shared.ui.compose.component.autosizetext.SuggestedFontSizesStatus.Companion.validSuggestedFontSizes
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toImmutableList
import timber.log.Timber
import kotlin.math.min

/**
 * See [gist](https://gist.github.com/inidamleader/b594d35362ebcf3cedf81055df519300)
 *
 * Composable function that automatically adjusts the text size to fit within given constraints, considering the ratio of line spacing to text size.
 *
 * Features:
 *  1. Best performance: Utilizes a dichotomous binary search algorithm for swift and optimal text size determination without unnecessary iterations.
 *  2. Alignment support: Supports six possible alignment values via the Alignment interface.
 *  3. Material Design 3 support.
 *  4. Font scaling support: User-initiated font scaling doesn't affect the visual rendering output.
 *  5. Multiline Support with maxLines Parameter.
 *
 * @param text the text to be displayed
 * @param modifier the [Modifier] to be applied to this layout node
 * @param color [Color] to apply to the text. If [Color.Unspecified], and [style] has no color set,
 * this will be [LocalContentColor].
 * @param suggestedFontSizes The suggested font sizes to choose from (Should be sorted from smallest to largest, not empty and contains only sp text unit).
 * @param suggestedFontSizesStatus Whether or not suggestedFontSizes is valid: not empty - contains oly sp text unit - sorted.
 * You can check validity by invoking [List<TextUnit>.suggestedFontSizesStatus].
 * @param stepGranularityTextSize The step size for adjusting the text size. this parameter is ignored if [suggestedFontSizes] is specified and [suggestedFontSizesStatus] is [SuggestedFontSizesStatus.VALID].
 * @param minTextSize The minimum text size allowed. this parameter is ignored if [suggestedFontSizes] is specified or [suggestedFontSizesStatus] is [SuggestedFontSizesStatus.VALID].
 * @param maxTextSize The maximum text size allowed.
 * @param fontStyle the typeface variant to use when drawing the letters (e.g., italic).
 * See [TextStyle.fontStyle].
 * @param fontWeight the typeface thickness to use when painting the text (e.g., [FontWeight.Bold]).
 * @param fontFamily the font family to be used when rendering the text. See [TextStyle.fontFamily].
 * @param letterSpacing the amount of space to add between each letter.
 * See [TextStyle.letterSpacing].
 * @param textDecoration the decorations to paint on the text (e.g., an underline).
 * See [TextStyle.textDecoration].
 * @param alignment The alignment of the text within its container.
 * @param overflow how visual overflow should be handled.
 * @param softWrap whether the text should break at soft line breaks. If false, the glyphs in the
 * text will be positioned as if there was unlimited horizontal space. If [softWrap] is false,
 * [overflow] and TextAlign may have unexpected effects.
 * @param maxLines An optional maximum number of lines for the text to span, wrapping if
 * necessary. If the text exceeds the given number of lines, it will be truncated according to
 * [overflow] and [softWrap]. It is required that 1 <= [minLines] <= [maxLines].
 * @param minLines The minimum height in terms of minimum number of visible lines. It is required
 * that 1 <= [minLines] <= [maxLines].
 * insert composables into text layout. See [InlineTextContent].
 * @param onTextLayout callback that is executed when a new text layout is calculated. A
 * [TextLayoutResult] object that callback provides contains paragraph information, size of the
 * text, baselines and other details. The callback can be used to add additional decoration or
 * functionality to the text. For example, to draw selection around the text.
 * @param style style configuration for the text such as color, font, line height etc.
 * @param lineSpaceRatio The ratio of line spacing to text size.
 *
 * @author Reda El Madini - For support, contact gladiatorkilo@gmail.com
 */
@Composable
fun AutoSizeText(
	text: String,
	modifier: Modifier = Modifier,
	color: Color = Color.Unspecified,
	suggestedFontSizes: ImmutableList<TextUnit> = listOf<TextUnit>().toImmutableList(),
	suggestedFontSizesStatus: SuggestedFontSizesStatus = SuggestedFontSizesStatus.UNKNOWN,
	stepGranularityTextSize: TextUnit = TextUnit.Unspecified,
	minTextSize: TextUnit = TextUnit.Unspecified,
	maxTextSize: TextUnit = TextUnit.Unspecified,
	fontStyle: FontStyle? = null,
	fontWeight: FontWeight? = null,
	fontFamily: FontFamily? = null,
	letterSpacing: TextUnit = TextUnit.Unspecified,
	textDecoration: TextDecoration? = null,
	alignment: Alignment = Alignment.TopStart,
	overflow: TextOverflow = TextOverflow.Clip,
	softWrap: Boolean = true,
	maxLines: Int = Int.MAX_VALUE,
	minLines: Int = 1,
	onTextLayout: (TextLayoutResult) -> Unit = {},
	style: TextStyle = LocalTextStyle.current,
	lineSpaceRatio: Float = style.lineHeight.value / style.fontSize.value,
) {
	AutoSizeText(
		text = AnnotatedString(text),
		modifier = modifier,
		color = color,
		suggestedFontSizes = suggestedFontSizes,
		suggestedFontSizesStatus = suggestedFontSizesStatus,
		stepGranularityTextSize = stepGranularityTextSize,
		minTextSize = minTextSize,
		maxTextSize = maxTextSize,
		fontStyle = fontStyle,
		fontWeight = fontWeight,
		fontFamily = fontFamily,
		letterSpacing = letterSpacing,
		textDecoration = textDecoration,
		alignment = alignment,
		overflow = overflow,
		softWrap = softWrap,
		maxLines = maxLines,
		minLines = minLines,
		onTextLayout = onTextLayout,
		style = style,
		lineSpacingRatio = lineSpaceRatio,
	)
}

/**
 * Composable function that automatically adjusts the text size to fit within given constraints using AnnotatedString, considering the ratio of line spacing to text size.
 *
 * Features:
 *  Similar to AutoSizeText(String), with support for AnnotatedString.
 *
 * @param inlineContent a map storing composables that replaces certain ranges of the text, used to
 * insert composables into text layout. See [InlineTextContent].
 * @see AutoSizeText
 */
@Composable
fun AutoSizeText(
	text: AnnotatedString,
	modifier: Modifier = Modifier,
	color: Color = Color.Unspecified,
	suggestedFontSizes: ImmutableList<TextUnit> = listOf<TextUnit>().toImmutableList(),
	suggestedFontSizesStatus: SuggestedFontSizesStatus = SuggestedFontSizesStatus.UNKNOWN,
	stepGranularityTextSize: TextUnit = TextUnit.Unspecified,
	minTextSize: TextUnit = TextUnit.Unspecified,
	maxTextSize: TextUnit = TextUnit.Unspecified,
	fontStyle: FontStyle? = null,
	fontWeight: FontWeight? = null,
	fontFamily: FontFamily? = null,
	letterSpacing: TextUnit = TextUnit.Unspecified,
	textDecoration: TextDecoration? = null,
	alignment: Alignment = Alignment.TopStart,
	overflow: TextOverflow = TextOverflow.Clip,
	softWrap: Boolean = true,
	maxLines: Int = Int.MAX_VALUE,
	minLines: Int = 1,
	inlineContent: ImmutableMap<String, InlineTextContent> = persistentMapOf(),
	onTextLayout: (TextLayoutResult) -> Unit = {},
	style: TextStyle = LocalTextStyle.current,
	lineSpacingRatio: Float = style.lineHeight.value / style.fontSize.value,
) {
	// Change font scale to 1F
	val newDensity = Density(density = LocalDensity.current.density, fontScale = 1F)
	CompositionLocalProvider(LocalDensity provides newDensity) {
		BoxWithConstraints(
			modifier = modifier,
			contentAlignment = alignment,
		) {
			val combinedTextStyle = LocalTextStyle.current + style.copy(
				color = color.takeIf { it.isSpecified } ?: style.color,
				fontStyle = fontStyle ?: style.fontStyle,
				fontWeight = fontWeight ?: style.fontWeight,
				fontFamily = fontFamily ?: style.fontFamily,
				letterSpacing = letterSpacing.takeIf { it.isSpecified } ?: style.letterSpacing,
				textDecoration = textDecoration ?: style.textDecoration,
				textAlign = when (alignment) {
					Alignment.TopStart, Alignment.CenterStart, Alignment.BottomStart -> TextAlign.Start
					Alignment.TopCenter, Alignment.Center, Alignment.BottomCenter -> TextAlign.Center
					Alignment.TopEnd, Alignment.CenterEnd, Alignment.BottomEnd -> TextAlign.End
					else -> TextAlign.Unspecified
				},
			)

			val layoutDirection = LocalLayoutDirection.current
			val density = LocalDensity.current
			val fontFamilyResolver = LocalFontFamilyResolver.current
			val textMeasurer = rememberTextMeasurer()
			val coercedLineSpacingRatio = lineSpacingRatio.takeIf { it.isFinite() && it >= 1 } ?: 1F
			val shouldMoveBackward: (TextUnit) -> Boolean = {
				shouldShrink(
					text = text,
					textStyle = combinedTextStyle.copy(
						fontSize = it,
						lineHeight = it * coercedLineSpacingRatio,
					),
					maxLines = maxLines,
					layoutDirection = layoutDirection,
					softWrap = softWrap,
					density = density,
					fontFamilyResolver = fontFamilyResolver,
					textMeasurer = textMeasurer,
				)
			}

			val electedFontSize = remember(
				key1 = suggestedFontSizes,
				key2 = suggestedFontSizesStatus,
			) {
				if (suggestedFontSizesStatus == SuggestedFontSizesStatus.VALID) {
					suggestedFontSizes
				} else {
					suggestedFontSizes.validSuggestedFontSizes
				}
			}?.let {
				remember(
					key1 = it,
					key2 = shouldMoveBackward,
				) {
					it.findElectedValue(shouldMoveBackward = shouldMoveBackward)
				}
			} ?: run {
				val candidateFontSizesIntProgress = rememberCandidateFontSizesIntProgress(
					density = density,
					containerDpSize = DpSize(maxWidth, maxHeight),
					maxTextSize = maxTextSize,
					minTextSize = minTextSize,
					stepGranularityTextSize = stepGranularityTextSize,
				)
				remember(
					key1 = candidateFontSizesIntProgress,
					key2 = shouldMoveBackward,
				) {
					candidateFontSizesIntProgress.findElectedValue(
						transform = { density.intPxToSp(it) },
						shouldMoveBackward = shouldMoveBackward,
					)
				}
			}

			if (electedFontSize == 0.sp) {
				Timber.w(
					"""The text cannot be displayed. Please consider the following options:
                      |  1. Providing 'suggestedFontSizes' with smaller values that can be utilized.
                      |  2. Decreasing the 'stepGranularityTextSize' value.
                      |  3. Adjusting the 'minTextSize' parameter to a suitable value and ensuring the overflow 
					  |  parameter is set to "TextOverflow.Ellipsis".
                    """.trimMargin(),
				)

				Text(
					text = text,
					overflow = overflow,
					softWrap = softWrap,
					maxLines = maxLines,
					minLines = minLines,
					inlineContent = inlineContent,
					onTextLayout = onTextLayout,
					style = combinedTextStyle.copy(
						fontSize = electedFontSize,
						lineHeight = electedFontSize * coercedLineSpacingRatio,
					),
				)
			}
		}
	}
}

private fun BoxWithConstraintsScope.shouldShrink(
	text: AnnotatedString,
	textStyle: TextStyle,
	maxLines: Int,
	layoutDirection: LayoutDirection,
	softWrap: Boolean,
	density: Density,
	fontFamilyResolver: FontFamily.Resolver,
	textMeasurer: TextMeasurer,
) = textMeasurer.measure(
	text = text,
	style = textStyle,
	overflow = TextOverflow.Clip,
	softWrap = softWrap,
	maxLines = maxLines,
	constraints = constraints,
	layoutDirection = layoutDirection,
	density = density,
	fontFamilyResolver = fontFamilyResolver,
).hasVisualOverflow

@Stable
@Composable
private fun rememberCandidateFontSizesIntProgress(
	density: Density,
	containerDpSize: DpSize,
	minTextSize: TextUnit = TextUnit.Unspecified,
	maxTextSize: TextUnit = TextUnit.Unspecified,
	stepGranularityTextSize: TextUnit = TextUnit.Unspecified,
): IntProgression {
	val max = remember(key1 = density, key2 = maxTextSize, key3 = containerDpSize) {
		val intSize = density.dpSizeRoundToIntSize(containerDpSize)
		min(intSize.width, intSize.height).let { max ->
			maxTextSize
				.takeIf { it.isSp }
				?.let { density.spRoundToPx(it) }
				?.coerceIn(range = 0..max)
				?: max
		}
	}

	val min = remember(key1 = density, key2 = minTextSize, key3 = max) {
		minTextSize
			.takeIf { it.isSp }
			?.let { density.spToIntPx(it) }
			?.coerceIn(range = 0..max)
			?: 0
	}

	val step = remember(
		key1 = listOf(
			density,
			min,
			max,
			stepGranularityTextSize,
		)
	) {
		stepGranularityTextSize
			.takeIf { it.isSp }
			?.let { density.spToIntPx(it) }
			?.coerceIn(1, max - min)
			?: 1
	}

	return remember(key1 = min, key2 = max, key3 = step) {
		min..max step step
	}
}

// This function works by using a binary search algorithm
fun <T> List<T>.findElectedValue(shouldMoveBackward: (T) -> Boolean) = run {
	indices.findElectedValue(
		transform = { this[it] },
		shouldMoveBackward = shouldMoveBackward,
	)
}

// This function works by using a binary search algorithm
private fun <T> IntProgression.findElectedValue(
	transform: (Int) -> T,
	shouldMoveBackward: (T) -> Boolean,
) = run {
	var low = first / step
	var high = last / step
	while (low <= high) {
		val mid = low + (high - low) / 2
		if (shouldMoveBackward(transform(mid * step))) high = mid - 1 else low = mid + 1
	}
	transform((high * step).coerceAtLeast(first * step))
}

enum class SuggestedFontSizesStatus {
	VALID, INVALID, UNKNOWN;

	companion object {

		val List<TextUnit>.getSuggestedFontSizesStatus
			get() = if (isNotEmpty() && fastAll { it.isSp } && sortedBy { it.value } == this) VALID else INVALID

		val List<TextUnit>.validSuggestedFontSizes
			get() = takeIf { it.isNotEmpty() } // Optimization: empty check first to immediately return null
				?.fastFilter { it.isSp }
				?.takeIf { it.isNotEmpty() }
				?.sortedBy { it.value }
	}
}

@Preview(widthDp = 200, heightDp = 100)
@Preview(widthDp = 200, heightDp = 30)
@Preview(widthDp = 60, heightDp = 30)
@Composable
private fun PreviewAutoSizeTextWithMaxLinesSetToIntMaxValue() {
	MaterialTheme {
		Surface(color = MaterialTheme.colorScheme.primary) {
			AutoSizeText(
				text = "This is a bunch of text that will be auto sized",
				modifier = Modifier.fillMaxSize(),
				alignment = Alignment.CenterStart,
				style = MaterialTheme.typography.bodyMedium,
			)
		}
	}
}

@Preview(widthDp = 200, heightDp = 100)
@Preview(widthDp = 200, heightDp = 30)
@Preview(widthDp = 60, heightDp = 30)
@Composable
private fun PreviewAutoSizeTextWithMinSizeSetTo14() {
	MaterialTheme {
		Surface(color = MaterialTheme.colorScheme.secondary) {
			AutoSizeText(
				text = "This is a bunch of text that will be auto sized",
				modifier = Modifier.fillMaxSize(),
				minTextSize = 14.sp,
				alignment = Alignment.CenterStart,
				overflow = TextOverflow.Ellipsis,
				style = MaterialTheme.typography.bodyMedium,
			)
		}
	}
}

@Preview(widthDp = 200, heightDp = 100)
@Preview(widthDp = 200, heightDp = 30)
@Preview(widthDp = 60, heightDp = 30)
@Composable
private fun PreviewAutoSizeTextWithMaxLinesSetToOne() {
	MaterialTheme {
		Surface(color = MaterialTheme.colorScheme.tertiary) {
			AutoSizeText(
				text = "This is a bunch of text that will be auto sized",
				modifier = Modifier.fillMaxSize(),
				alignment = Alignment.Center,
				maxLines = 1,
				style = MaterialTheme.typography.bodyMedium
			)
		}
	}
}

@Preview(widthDp = 100, heightDp = 50)
@Preview(widthDp = 50, heightDp = 100)
@Composable
private fun PreviewAutoSizeTextWithMCharacter() {
	MaterialTheme {
		Surface(color = MaterialTheme.colorScheme.error) {
			AutoSizeText(
				text = "m",
				modifier = Modifier.fillMaxSize(),
				alignment = Alignment.Center,
				style = MaterialTheme.typography.bodyMedium,
				lineSpaceRatio = 1F,
			)
		}
	}
}

@Preview(widthDp = 100, heightDp = 50)
@Preview(widthDp = 50, heightDp = 100)
@Composable
private fun PreviewAutoSizeTextWithYCharacter() {
	MaterialTheme {
		Surface(color = MaterialTheme.colorScheme.error) {
			AutoSizeText(
				text = "y",
				modifier = Modifier.fillMaxSize(),
				alignment = Alignment.Center,
				style = MaterialTheme.typography.bodyMedium.copy(
					lineHeightStyle = LineHeightStyle(
						alignment = LineHeightStyle.Alignment.Center,
						trim = LineHeightStyle.Trim.Both,
					)
				),
			)
		}
	}
}
