package com.zsoltbertalan.flickslate.account.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.zsoltbertalan.flickslate.account.domain.model.FavoriteMovie
import com.zsoltbertalan.flickslate.account.domain.model.FavoriteTvShow
import com.zsoltbertalan.flickslate.account.domain.model.RatedMovie
import com.zsoltbertalan.flickslate.account.domain.model.RatedTvEpisode
import com.zsoltbertalan.flickslate.account.domain.model.RatedTvShow
import com.zsoltbertalan.flickslate.account.ui.R
import com.zsoltbertalan.flickslate.account.ui.favorites.FavoriteShowCard
import com.zsoltbertalan.flickslate.account.ui.favorites.FavoritesViewModel
import com.zsoltbertalan.flickslate.account.ui.ratings.EmptyRatedListCard
import com.zsoltbertalan.flickslate.account.ui.ratings.RatedShowCard
import com.zsoltbertalan.flickslate.account.ui.ratings.RatingsViewModel
import com.zsoltbertalan.flickslate.shared.domain.model.Account
import com.zsoltbertalan.flickslate.shared.ui.compose.component.ListTitle
import com.zsoltbertalan.flickslate.shared.ui.compose.component.paging.PaginatedLazyRow
import com.zsoltbertalan.flickslate.shared.ui.compose.component.paging.PaginationInternalState
import com.zsoltbertalan.flickslate.shared.ui.compose.design.Colors
import com.zsoltbertalan.flickslate.shared.ui.compose.design.Dimens
import com.zsoltbertalan.flickslate.shared.ui.compose.design.FlickSlateTheme
import com.zsoltbertalan.flickslate.shared.ui.compose.design.FlickSlateTypography
import com.zsoltbertalan.flickslate.shared.ui.compose.design.titleMediumBold

@Composable
fun LoggedInComponent(
	colorScheme: ColorScheme,
	account: Account,
	logout: () -> Unit,
	navigateToMovieDetails: (Int) -> Unit,
	navigateToTvShowDetails: (Int) -> Unit,
	navigateToTvEpisodeDetails: (Int, Int, Int) -> Unit,
	modifier: Modifier = Modifier,
	ratingsViewModel: RatingsViewModel? = hiltViewModel<RatingsViewModel>(),
	favoritesViewModel: FavoritesViewModel? = hiltViewModel<FavoritesViewModel>(),
) {
	var selectedTabIndex by rememberSaveable(account.id) { mutableIntStateOf(0) }
	val tabs = listOf("Ratings", "Favorites")

	LazyColumn(
		modifier = modifier
			.fillMaxSize()
			.background(colorScheme.surface),
	) {
		item {
			// Header (scrolls away)
			BasicText(
				text = "Welcome ${account.displayName}",
				style = FlickSlateTypography.titleMediumBold,
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = Dimens.marginLarge)
					.padding(top = Dimens.marginNormal, bottom = Dimens.marginLarge)
			)
		}

		item {
			Column(modifier = Modifier.padding(horizontal = Dimens.marginLarge)) {
				AccountDetailRow(label = "Username:", value = account.username)
				AccountDetailRow(label = "User ID:", value = account.id.toString())
				AccountDetailRow(label = "Language:", value = account.language)
				AccountDetailRow(
					label = "Include Adult Content:",
					value = if (account.includeAdult) "Yes" else "No"
				)

				Spacer(modifier = Modifier.height(Dimens.marginLarge))

				Button(logout) {
					Text("Logout")
				}
			}
		}

		// Reduce the gap between header and tabs
		item { Spacer(modifier = Modifier.height(Dimens.marginNormal)) }

		stickyHeader {
			PrimaryTabRow(selectedTabIndex = selectedTabIndex) {
				tabs.forEachIndexed { index, title ->
					Tab(
						selected = selectedTabIndex == index,
						onClick = { selectedTabIndex = index },
						text = { Text(text = title) }
					)
				}
			}
		}

		// Tab content rendered into the SAME LazyColumn (no nested vertical scrollers)
		if (ratingsViewModel != null && favoritesViewModel != null) {
			when (selectedTabIndex) {
				0 -> ratingsTabContent(
					ratingsViewModel = ratingsViewModel,
					navigateToMovieDetails = navigateToMovieDetails,
					navigateToTvShowDetails = navigateToTvShowDetails,
					navigateToTvEpisodeDetails = navigateToTvEpisodeDetails,
				)

				1 -> favoritesTabContent(
					favoritesViewModel = favoritesViewModel,
					navigateToMovieDetails = navigateToMovieDetails,
					navigateToTvShowDetails = navigateToTvShowDetails,
				)
			}
		}
	}
}

private fun LazyListScope.ratingsTabContent(
	ratingsViewModel: RatingsViewModel,
	navigateToMovieDetails: (Int) -> Unit,
	navigateToTvShowDetails: (Int) -> Unit,
	navigateToTvEpisodeDetails: (Int, Int, Int) -> Unit,
) {
	item { ListTitle(title = androidx.compose.ui.res.stringResource(id = R.string.rated_movies)) }

	item {
		val ratedMovies = ratingsViewModel.ratedMoviesPaginationState
		val ratedMoviesState = ratedMovies.internalState.value
		if (ratedMoviesState is PaginationInternalState.Loaded && ratedMoviesState.items.isEmpty()) {
			EmptyRatedListCard(
				text = androidx.compose.ui.res.stringResource(id = R.string.no_rated_movies),
				modifier = Modifier.padding(horizontal = Dimens.marginLarge)
			)
		} else {
			PaginatedLazyRow(
				contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = Dimens.marginLarge),
				horizontalArrangement = Arrangement.spacedBy(Dimens.marginLarge),
				paginationState = ratedMovies
			) {
				items(ratedMovies.allItems) { ratedMovie: RatedMovie ->
					RatedShowCard(
						modifier = Modifier
							.clickable { navigateToMovieDetails(ratedMovie.movie.id) },
						title = ratedMovie.movie.title,
						posterPath = ratedMovie.movie.posterPath,
						rating = ratedMovie.rating
					)
				}
			}
		}
	}

	item {
		ListTitle(
			title = androidx.compose.ui.res.stringResource(id = R.string.rated_tv_shows),
			modifier = Modifier.padding(top = Dimens.marginLarge)
		)
	}

	item {
		val ratedTvShows = ratingsViewModel.ratedTvShowsPaginationState
		val ratedTvShowsState = ratedTvShows.internalState.value
		if (ratedTvShowsState is PaginationInternalState.Loaded && ratedTvShowsState.items.isEmpty()) {
			EmptyRatedListCard(
				text = androidx.compose.ui.res.stringResource(id = R.string.no_rated_tv_shows),
				modifier = Modifier.padding(horizontal = Dimens.marginLarge)
			)
		} else {
			PaginatedLazyRow(
				contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = Dimens.marginLarge),
				horizontalArrangement = Arrangement.spacedBy(Dimens.marginLarge),
				paginationState = ratedTvShows,
			) {
				items(ratedTvShows.allItems) { ratedTvShow: RatedTvShow ->
					RatedShowCard(
						modifier = Modifier.clickable { navigateToTvShowDetails(ratedTvShow.tvShow.id) },
						title = ratedTvShow.tvShow.name,
						posterPath = ratedTvShow.tvShow.posterPath,
						rating = ratedTvShow.rating
					)
				}
			}
		}
	}

	item {
		ListTitle(
			title = androidx.compose.ui.res.stringResource(id = R.string.rated_tv_episodes),
			modifier = Modifier.padding(top = Dimens.marginLarge)
		)
	}

	item {
		val ratedEpisodes = ratingsViewModel.ratedTvEpisodesPaginationState
		val ratedEpisodesState = ratedEpisodes.internalState.value
		if (ratedEpisodesState is PaginationInternalState.Loaded && ratedEpisodesState.items.isEmpty()) {
			EmptyRatedListCard(
				text = androidx.compose.ui.res.stringResource(id = R.string.no_rated_tv_episodes),
				modifier = Modifier.padding(horizontal = Dimens.marginLarge)
			)
		} else {
			PaginatedLazyRow(
				contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = Dimens.marginLarge),
				horizontalArrangement = Arrangement.spacedBy(Dimens.marginLarge),
				paginationState = ratedEpisodes,
			) {
				items(ratedEpisodes.allItems) { ratedEpisode: RatedTvEpisode ->
					RatedShowCard(
						modifier = Modifier.clickable {
							navigateToTvEpisodeDetails(
								ratedEpisode.tvEpisodeDetail.showId,
								ratedEpisode.tvEpisodeDetail.seasonNumber,
								ratedEpisode.tvEpisodeDetail.episodeNumber
							)
						},
						title = ratedEpisode.tvEpisodeDetail.name ?: "",
						posterPath = ratedEpisode.tvEpisodeDetail.stillPath,
						rating = ratedEpisode.rating
					)
				}
			}
		}
	}
}

private fun LazyListScope.favoritesTabContent(
	favoritesViewModel: FavoritesViewModel,
	navigateToMovieDetails: (Int) -> Unit,
	navigateToTvShowDetails: (Int) -> Unit,
) {
	item { ListTitle(title = androidx.compose.ui.res.stringResource(id = R.string.favorite_movies)) }

	item {
		val favoriteMovies = favoritesViewModel.favoriteMoviesPaginationState
		val favoriteMoviesState = favoriteMovies.internalState.value
		if (favoriteMoviesState is PaginationInternalState.Loaded && favoriteMoviesState.items.isEmpty()) {
			EmptyRatedListCard(
				text = androidx.compose.ui.res.stringResource(id = R.string.no_favorite_movies),
				modifier = Modifier.padding(horizontal = Dimens.marginLarge)
			)
		} else {
			PaginatedLazyRow(
				contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = Dimens.marginLarge),
				horizontalArrangement = Arrangement.spacedBy(Dimens.marginLarge),
				paginationState = favoriteMovies
			) {
				items(favoriteMovies.allItems) { favoriteMovie: FavoriteMovie ->
					FavoriteShowCard(
						modifier = Modifier.clickable { navigateToMovieDetails(favoriteMovie.movie.id) },
						title = favoriteMovie.movie.title,
						posterPath = favoriteMovie.movie.posterPath,
					)
				}
			}
		}
	}

	item {
		ListTitle(
			title = androidx.compose.ui.res.stringResource(id = R.string.favorite_tv_shows),
			modifier = Modifier.padding(top = Dimens.marginLarge)
		)
	}

	item {
		val favoriteTvShows = favoritesViewModel.favoriteTvShowsPaginationState
		val favoriteTvShowsState = favoriteTvShows.internalState.value
		if (favoriteTvShowsState is PaginationInternalState.Loaded && favoriteTvShowsState.items.isEmpty()) {
			EmptyRatedListCard(
				text = androidx.compose.ui.res.stringResource(id = R.string.no_favorite_tv_shows),
				modifier = Modifier.padding(horizontal = Dimens.marginLarge)
			)
		} else {
			PaginatedLazyRow(
				contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = Dimens.marginLarge),
				horizontalArrangement = Arrangement.spacedBy(Dimens.marginLarge),
				paginationState = favoriteTvShows,
			) {
				items(favoriteTvShows.allItems) { favoriteTvShow: FavoriteTvShow ->
					FavoriteShowCard(
						modifier = Modifier.clickable { navigateToTvShowDetails(favoriteTvShow.tvShow.id) },
						title = favoriteTvShow.tvShow.name,
						posterPath = favoriteTvShow.tvShow.posterPath,
					)
				}
			}
		}
	}
}

@Composable
private fun AccountDetailRow(label: String, value: String) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(vertical = 4.dp),
		horizontalArrangement = Arrangement.SpaceBetween // Arrange label and value on opposite ends
	) {
		Text(
			text = label,
			style = MaterialTheme.typography.bodyLarge,
			color = MaterialTheme.colorScheme.onSurfaceVariant
		)
		Text(
			text = value,
			style = MaterialTheme.typography.bodyLarge,
			color = MaterialTheme.colorScheme.onSurface
		)
	}
}

@Preview
@Composable
private fun PreviewAutoSizeTextWithMaxLinesSetToOne() {
	FlickSlateTheme {
		LoggedInComponent(
			modifier = Modifier.fillMaxSize(),
			colorScheme = Colors,
			account = Account(
				username = "john.doe",
				displayName = "John Doe",
				language = "en-US",
				id = 12345,
				includeAdult = false,
			),
			logout = {},
			navigateToMovieDetails = {},
			navigateToTvShowDetails = {},
			navigateToTvEpisodeDetails = { _, _, _ -> },
			ratingsViewModel = null,
			favoritesViewModel = null
		)
	}
}
