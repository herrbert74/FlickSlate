package com.zsoltbertalan.flickslate.shared.ui.compose.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.zsoltbertalan.flickslate.shared.ui.compose.design.FlickSlateTheme
import com.zsoltbertalan.flickslate.shared.ui.compose.design.smallDimensions

@Composable
fun FilledButton(
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	enabled: Boolean = true,
	shape: Shape = RoundedCornerShape(smallDimensions.marginNormal),
	buttonColors: ButtonColors = ButtonDefaults.buttonColors(),
	content: @Composable RowScope.() -> Unit,
) {
	Button(
		onClick = onClick,
		modifier = modifier.defaultMinSize(
			minWidth = smallDimensions.buttonWidthShort,
			minHeight = smallDimensions.buttonHeight
		),
		enabled = enabled,
		colors = buttonColors,
		content = content,
		shape = shape,
	)
}

@PreviewLightDark
@Composable
private fun FilledTextButtonPreview() {
	FlickSlateTheme {
		Surface {
			FilledButton(
				onClick = {},
				modifier = Modifier
					.fillMaxWidth()
					.padding(2.dp)
					.background(color = MaterialTheme.colorScheme.surface),
			) {
				Text(
					text = "Season 1",
					style = MaterialTheme.typography.bodyMedium,
					color = MaterialTheme.colorScheme.onPrimary,
				)
			}
		}
	}
}
