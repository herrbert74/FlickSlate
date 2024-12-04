package com.zsoltbertalan.flickslate.tv.ui.tvdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.zsoltbertalan.flickslate.shared.compose.design.Colors
import com.zsoltbertalan.flickslate.shared.compose.design.Dimens
import com.zsoltbertalan.flickslate.tv.domain.model.TvDetail
import com.zsoltbertalan.flickslate.shared.compose.component.GenreChips

@Composable
fun TvDetailScreen(
	detail: State<TvDetail>,
	modifier: Modifier = Modifier,
	popBackStack: () -> Boolean
) {
	Scaffold(
		modifier = modifier.fillMaxSize(),
		topBar = {
			TopAppBar(title = { Text("TV Details") },
				navigationIcon = {
					IconButton(onClick = { popBackStack() }) {
						Icon(
							imageVector = Icons.AutoMirrored.Filled.ArrowBack,
							contentDescription = "Finish",
							tint = Colors.onSurface
						)
					}
				})
		}
	) { paddingValues ->
		LazyColumn(Modifier.padding(paddingValues)) {
			item {
				Image(
					painter = rememberAsyncImagePainter("https://image.tmdb.org/t/p/original" + detail.value.backdropPath),
					contentDescription = "",
					modifier = Modifier
						.fillMaxWidth()
						.aspectRatio(1.0f),
					contentScale = ContentScale.Crop
				)
			}
			item {
				Row(Modifier.height(Dimens.listSingleItemHeight)) {
					Text(
						modifier = Modifier.padding(16.dp),
						text = detail.value.title ?: ""
					)
					VerticalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Colors.onSurface)
					Text(
						modifier = Modifier.padding(16.dp),
						text = detail.value.voteAverage.toString()
					)
				}
			}
			item {
				Text(
					modifier = Modifier.padding(16.dp),
					text = "Story Line"
				)
				Text(
					modifier = Modifier.padding(16.dp),
					text = detail.value.overview ?: ""
				)
			}
			item {
				Column(modifier = Modifier.padding(horizontal = Dimens.marginLarge)) {
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
}
