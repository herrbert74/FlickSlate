package com.zsoltbertalan.flickslate.presentation.ui.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.zsoltbertalan.flickslate.domain.model.Movie
import com.zsoltbertalan.flickslate.domain.model.MovieCardType
import com.zsoltbertalan.flickslate.presentation.component.ListTitle
import com.zsoltbertalan.flickslate.presentation.component.ShowCard
import com.zsoltbertalan.flickslate.presentation.component.paging.FirstPageErrorIndicator
import com.zsoltbertalan.flickslate.presentation.component.paging.FirstPageProgressIndicator
import com.zsoltbertalan.flickslate.presentation.component.paging.NewPageErrorIndicator
import com.zsoltbertalan.flickslate.presentation.component.paging.NewPageProgressIndicator
import com.zsoltbertalan.flickslate.presentation.component.paging.PaginatedLazyColumn
import com.zsoltbertalan.flickslate.presentation.component.paging.PaginationState
import com.zsoltbertalan.flickslate.presentation.design.Colors
import com.zsoltbertalan.flickslate.presentation.util.navigate

@Composable
fun GenreDetailScreen(
	genreMoviesPaginatedState: PaginationState<Int, Movie>,
	genreName: String,
	popBackStack: () -> Boolean,
	modifier: Modifier = Modifier,
	popTo: (Int) -> Unit,
) {

	Scaffold(
		modifier = modifier.fillMaxSize(),
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

		Column(
			modifier = Modifier
				.padding(paddingValues)
		) {

			ListTitle(title = genreName)

			PaginatedLazyColumn(
				genreMoviesPaginatedState,
				firstPageProgressIndicator = { FirstPageProgressIndicator() },
				newPageProgressIndicator = { NewPageProgressIndicator() },
				firstPageErrorIndicator = { e ->
					FirstPageErrorIndicator(
						exception = e,
						onRetryClick = {
							genreMoviesPaginatedState.retryLastFailedRequest()
						}
					)
				},
				newPageErrorIndicator = { e ->
					NewPageErrorIndicator(
						exception = e,
						onRetryClick = {
							genreMoviesPaginatedState.retryLastFailedRequest()
						}
					)
				},
				modifier = Modifier
					.fillMaxHeight(),
			) {
				itemsIndexed(
					genreMoviesPaginatedState.allItems,
				) { _, item ->
					ShowCard(
						modifier = Modifier.navigate(item.id, popTo),
						title = item.title,
						voteAverage = item.voteAverage,
						overview = item.overview,
						posterPath = item.posterPath,
						cardType = MovieCardType.FULL
					)
				}
			}
		}
	}

}
