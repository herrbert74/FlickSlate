package com.zsoltbertalan.flickslate.main

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import com.zsoltbertalan.flickslate.shared.ui.compose.design.Colors

private const val VIEWPORT_SIZE = 36f
private const val EYE_X_POS = 21f
private const val EYE_Y_POS = 22f
private const val EYE_RADIUS = 2f
private const val MAX_SCALE = 100f
private const val ANIMATION_DURATION = 1000

@Composable
fun ClapperboardTransition(
	isVisible: Boolean,
	modifier: Modifier = Modifier,
	onAnimationEnd: () -> Unit = {}
) {
	val scale = remember { Animatable(1f) }

	val currentOnAnimationEnd by rememberUpdatedState(onAnimationEnd)

	LaunchedEffect(isVisible) {
		if (!isVisible) {
			scale.animateTo(
				targetValue = MAX_SCALE,
				animationSpec = tween(durationMillis = ANIMATION_DURATION, easing = FastOutSlowInEasing)
			)
			currentOnAnimationEnd()
		}
	}

	val overlayColor = Colors.surfaceDim

	Canvas(modifier = modifier) {
		val iconSize = size.width // Assuming square 288.dp
		val eyeX = (EYE_X_POS / VIEWPORT_SIZE) * iconSize
		val eyeY = (EYE_Y_POS / VIEWPORT_SIZE) * iconSize
		val eyeRadius = (EYE_RADIUS / VIEWPORT_SIZE) * iconSize

		drawCircle(
			color = overlayColor,
			radius = eyeRadius * scale.value,
			center = Offset(eyeX, eyeY)
		)
	}
}
