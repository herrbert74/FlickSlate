package com.zsoltbertalan.flickslate.shared.ui.compose.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zsoltbertalan.flickslate.shared.ui.compose.design.FixedColorScheme
import com.zsoltbertalan.flickslate.shared.ui.compose.design.LocalFixedColors
import com.zsoltbertalan.flickslate.shared.ui.compose.design.primaryContainerLight
import com.zsoltbertalan.flickslate.shared.ui.compose.design.titleMediumBold
import kotlin.math.roundToInt

private val CIRCLE_SIZE = 64.dp
private val CIRCLE_STROKE_WIDTH = 4.dp
private val ARC_PADDING = 2.dp
private val RATING_TEXT_SIZE = 14.sp
private const val RATING_TO_PERCENTAGE = 10
private const val PERCENTAGE = 100
private const val PERCENTAGE_FLOAT = 100f
private const val CIRCLE_DEGREES = 360f

@Composable
fun FloatingRatingCircle(rating: Float, modifier: Modifier = Modifier) {
	val percentage = (rating * RATING_TO_PERCENTAGE).roundToInt().coerceIn(0, PERCENTAGE)
	val fixedColors = LocalFixedColors.current

	Box(
		modifier = modifier
			.size(CIRCLE_SIZE)
			.padding(end = 16.dp)
	) {
		Canvas(
			modifier = Modifier.size(CIRCLE_SIZE)
		) {
			drawRatingCircle(percentage, fixedColors)
		}

		Text(
			text = "$percentage%",
			modifier = Modifier
				.align(Alignment.Center),
			fontSize = RATING_TEXT_SIZE,
			fontWeight = FontWeight.Bold,
			color = fixedColors.inverseOnSurfaceFixed,
			textAlign = TextAlign.Center,
			style = MaterialTheme.typography.titleMediumBold,
		)
	}
}

private fun DrawScope.drawRatingCircle(percentage: Int, fixedColors: FixedColorScheme) {
	val center = Offset(size.width / 2, size.height / 2)
	val radius = size.minDimension / 2 - CIRCLE_STROKE_WIDTH.toPx() / 2
	val arcPadding = ARC_PADDING.toPx()

	// Draw full background circle
	drawCircle(
		color = fixedColors.inverseSurfaceFixed,
		radius = radius,
		center = center,
	)

	// Draw progress arc with padding
	if (percentage > 0) {
		val sweepAngle = (percentage * CIRCLE_DEGREES) / PERCENTAGE_FLOAT
		val arcRadius = radius - arcPadding
		drawArc(
			color = when {
				percentage >= 70 -> primaryContainerLight
				percentage >= 50 -> fixedColors.quinaryFixed
				else -> fixedColors.tertiaryFixed
			},
			startAngle = -90f,
			sweepAngle = sweepAngle,
			useCenter = false,
			size = Size(arcRadius * 2, arcRadius * 2),
			topLeft = Offset(center.x - arcRadius, center.y - arcRadius),
			style = Stroke(width = CIRCLE_STROKE_WIDTH.toPx())
		)
	}
}
