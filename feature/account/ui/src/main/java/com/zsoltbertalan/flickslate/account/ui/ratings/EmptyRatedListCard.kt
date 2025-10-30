package com.zsoltbertalan.flickslate.account.ui.ratings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zsoltbertalan.flickslate.account.ui.R
import com.zsoltbertalan.flickslate.shared.ui.compose.design.Colors
import com.zsoltbertalan.flickslate.shared.ui.compose.design.FlickSlateTheme

@Composable
fun EmptyRatedListCard(
	text: String,
	modifier: Modifier = Modifier,
) {
	Card(
		modifier = modifier.width(IMAGE_WIDTH),
		shape = MaterialTheme.shapes.large,
		colors = CardDefaults.cardColors(
			containerColor = Colors.surfaceContainerHighest,
		)
	) {
		Column(
			modifier = Modifier.padding(8.dp),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Icon(
				painter = painterResource(id = com.zsoltbertalan.flickslate.shared.ui.R.drawable.ic_movie),
				contentDescription = null,
				modifier = Modifier.width(IMAGE_WIDTH).height(IMAGE_HEIGHT)
			)
			Spacer(modifier = Modifier.height(8.dp))
			Text(
				text = text,
				style = MaterialTheme.typography.bodySmall,
				textAlign = TextAlign.Center,
				minLines = 4
			)
		}
	}
}

@Preview(showBackground = true)
@Composable
private fun EmptyRatedListCardPreview() {
	FlickSlateTheme {
		EmptyRatedListCard(text = "You have no rated movies")
	}
}
