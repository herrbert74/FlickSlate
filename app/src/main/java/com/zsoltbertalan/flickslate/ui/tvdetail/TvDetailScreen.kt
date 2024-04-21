package com.zsoltbertalan.flickslate.ui.tvdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.zsoltbertalan.flickslate.domain.model.TvDetail
import com.zsoltbertalan.flickslate.ui.component.GenreChips

@Composable
fun TvDetailScreen(
	modifier: Modifier = Modifier,
	detail: State<TvDetail>
) {
	LazyColumn(
		modifier = modifier
			.fillMaxSize()
			.padding(bottom = 20.dp)
	) {
		item {
			Image(
				painter = rememberAsyncImagePainter("https://image.tmdb.org/t/p/original" + detail.value.backdropPath),
				contentDescription = "",
				modifier = modifier
					.fillMaxWidth()
					.aspectRatio(1.0f),
				contentScale = ContentScale.Crop
			)
		}
		item {
			Row(modifier = Modifier.height(50.dp)) {
				Text(
					modifier = modifier.padding(16.dp),
					text = detail.value.title ?: ""
				)
				VerticalDivider(modifier = Modifier.padding(vertical = 16.dp))
				Text(
					modifier = modifier.padding(16.dp),
					text = detail.value.voteAverage.toString()
				)
			}
		}
		item {
			Text(
				modifier = modifier.padding(16.dp),
				text = "Story Line"
			)
			Text(
				modifier = modifier.padding(16.dp),
				text = detail.value.overview ?: ""
			)
		}
		item {
			Column(modifier = Modifier.padding(horizontal = 16.dp)) {
				Text(
					text = "Genres"
				)
				detail.value.genres.takeIf { it.isNotEmpty() }?.let {
					GenreChips(it)
				}
			}
		}
	}
}
