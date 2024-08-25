package com.zsoltbertalan.flickslate.shared.compose.component.paging

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FirstPageProgressIndicator(modifier: Modifier = Modifier) {
	Box(
		modifier = modifier.fillMaxSize(),
		contentAlignment = Alignment.Center
	) {
		CircularProgressIndicator()
	}
}

@Composable
fun NewPageProgressIndicator(modifier: Modifier = Modifier) {
	Box(
		modifier = modifier.fillMaxWidth().padding(vertical = 16.dp),
		contentAlignment = Alignment.Center
	) {
		Box(
			modifier = Modifier,
		) {
			LinearProgressIndicator()
		}
	}
}

@Composable
fun FirstPageErrorIndicator(
	exception: Exception,
	modifier: Modifier = Modifier,
	onRetryClick: () -> Unit = {},
) {
	Box(
		modifier = modifier.fillMaxSize(),
		contentAlignment = Alignment.Center
	) {
		Column(
			modifier = Modifier.fillMaxWidth(),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Text(
				exception.message!!,
				textAlign = TextAlign.Center,
				fontWeight = FontWeight.Bold,
				fontSize = 18.sp
			)

			Spacer(modifier = Modifier.height(32.dp))

			Button(
				onClick = onRetryClick
			) {
				Text("Retry")
			}
		}
	}
}

@Composable
fun NewPageErrorIndicator(
	exception: Exception,
	modifier: Modifier = Modifier,
	onRetryClick: () -> Unit = {},
) {
	Box(
		modifier = modifier.fillMaxSize(),
		contentAlignment = Alignment.Center
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically
		) {
			Text(
				exception.message!!,
				textAlign = TextAlign.Center,
				fontWeight = FontWeight.Bold,
				fontSize = 18.sp
			)

			Spacer(modifier = Modifier.width(32.dp))

			Button(
				onClick = onRetryClick
			) {
				Text("Retry")
			}
		}
	}
}
