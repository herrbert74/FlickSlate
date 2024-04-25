package com.zsoltbertalan.flickslate.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.zsoltbertalan.flickslate.R
import com.zsoltbertalan.flickslate.design.Colors
import com.zsoltbertalan.flickslate.domain.model.Movie
import com.zsoltbertalan.flickslate.domain.model.MovieCardType
import com.zsoltbertalan.flickslate.ext.navigate
import com.zsoltbertalan.flickslate.ui.component.ListTitle
import com.zsoltbertalan.flickslate.ui.component.ShowCard
import com.zsoltbertalan.flickslate.ui.component.ShowLoading

@Composable
fun GenreDetailScreen(
	genreDetailList: LazyPagingItems<Movie>,
	genreName: String,
	popBackStack: () -> Boolean,
	popTo: (Int) -> Unit,
) {

	Scaffold(
		modifier = Modifier.fillMaxSize(),
		topBar = {
			TopAppBar(title = { Text("Genre Details") },
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
		LazyColumn(
			modifier = Modifier
				.background(Colors.surface)
				.padding(paddingValues)
		) {
			showMovies(genreDetailList, genreName, popTo)
		}
	}
}

private fun LazyListScope.showMovies(
	genreMovies: LazyPagingItems<Movie>,
	genreName: String,
	popTo: (Int) -> Unit,
) {

	item {
		ListTitle(title = genreName)
	}

	items(genreMovies.itemCount) { index ->
		genreMovies[index].let {
			it?.let {
				ShowCard(
					modifier = Modifier.navigate(it.id, popTo),
					title = it.title,
					voteAverage = it.voteAverage,
					overview = it.overview,
					posterPath = it.posterPath,
					cardType = MovieCardType.FULL
				)
			}

		}
	}

	item {
		Spacer(modifier = Modifier.height(20.dp))
	}

	when {
		genreMovies.loadState.refresh is LoadState.Loading -> {
			item {
				ShowLoading(
					text = stringResource(id = R.string.genre_movies, genreName)
				)
			}
		}

		genreMovies.loadState.append is LoadState.Loading -> {
			item {
				ShowLoading(
					text = stringResource(id = R.string.genre_movies, genreName)
				)
			}
		}

		genreMovies.loadState.refresh is LoadState.Error -> {
			item {
				Text(text = "Not Loading")
			}
		}
	}
}
