package com.zsoltbertalan.flickslate.main.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.zsoltbertalan.flickslate.account.ui.main.AccountScreen
import com.zsoltbertalan.flickslate.account.ui.main.AccountViewModel
import com.zsoltbertalan.flickslate.movies.ui.main.MoviesScreen
import com.zsoltbertalan.flickslate.movies.ui.main.MoviesViewModel
import com.zsoltbertalan.flickslate.movies.ui.moviedetails.MovieDetailScreen
import com.zsoltbertalan.flickslate.search.ui.main.GenreDetailScreen
import com.zsoltbertalan.flickslate.search.ui.main.SearchScreen
import com.zsoltbertalan.flickslate.search.ui.main.SearchViewModel
import com.zsoltbertalan.flickslate.shared.ui.compose.component.paging.PaginationInternalState
import com.zsoltbertalan.flickslate.shared.ui.navigation.LocalResultStore
import com.zsoltbertalan.flickslate.shared.ui.navigation.rememberResultStore
import com.zsoltbertalan.flickslate.tv.ui.main.TvScreen
import com.zsoltbertalan.flickslate.tv.ui.main.TvViewModel
import com.zsoltbertalan.flickslate.tv.ui.seasondetail.TvSeasonDetailScreen
import com.zsoltbertalan.flickslate.tv.ui.tvdetail.TvDetailScreen
import kotlinx.coroutines.launch

@Composable
fun NavHostContainer(
	navigationState: NavigationState,
	navigator: Navigator,
	paddingValues: PaddingValues,
	setTitle: (String) -> Unit,
	setBackgroundColor: (Color) -> Unit,
	setMoviesListsReady: (Boolean) -> Unit,
	modifier: Modifier = Modifier,
	viewModel: MoviesViewModel = hiltViewModel<MoviesViewModel>(),
	searchViewModel: SearchViewModel = hiltViewModel<SearchViewModel>(),
	tvViewModel: TvViewModel = hiltViewModel<TvViewModel>(),
	accountViewModel: AccountViewModel = hiltViewModel<AccountViewModel>(),
) {
	val resultStore = rememberResultStore()

	val moviesListsReady by remember {
		derivedStateOf {
			val popularReady = viewModel.popularMoviesPaginationState.internalState.value.isFirstPageCompleted
			val nowPlayingReady = viewModel.nowPlayingMoviesPaginationState.internalState.value.isFirstPageCompleted
			val upcomingReady = viewModel.upcomingMoviesPaginationState.internalState.value.isFirstPageCompleted
			popularReady && nowPlayingReady && upcomingReady
		}
	}

	val currentSetMoviesListsReady by rememberUpdatedState(setMoviesListsReady)

	LaunchedEffect(moviesListsReady) {
		if (moviesListsReady) currentSetMoviesListsReady(true)
	}

	val entryProvider: (NavKey) -> NavEntry<NavKey> = entryProvider {
		entry<Destination.Movies> {
			setTitle(stringResource(com.zsoltbertalan.flickslate.shared.ui.R.string.app_name))
			val popularMovies = viewModel.popularMoviesPaginationState
			val nowPlaying = viewModel.nowPlayingMoviesPaginationState
			val upcoming = viewModel.upcomingMoviesPaginationState
			MoviesScreen(
				popularMoviesPaginatedState = popularMovies,
				nowPlayingMoviesPaginatedState = nowPlaying,
				upcomingMoviesPaginatedState = upcoming,
			) { id ->
				navigator.navigate(Destination.MovieDetails(id))
			}
		}
		entry<Destination.Tv> {
			setTitle(stringResource(com.zsoltbertalan.flickslate.shared.ui.R.string.app_name))
			val topRatedTv = tvViewModel.tvPaginationState
			TvScreen(paginatedState = topRatedTv) { id ->
				navigator.navigate(Destination.TvDetails(id))
			}
		}
		entry<Destination.Search> {
			setTitle(stringResource(com.zsoltbertalan.flickslate.shared.ui.R.string.app_name))
			val coroutineScope = rememberCoroutineScope()
			val searchState by searchViewModel.searchStateData.collectAsStateWithLifecycle()
			SearchScreen(
				searchState = searchState,
				searchEvent = {
					coroutineScope.launch {
						searchViewModel.emitEvent(it)
					}
				},
				navigateToGenreDetails = { id, name ->
					navigator.navigate(Destination.GenreMovies(id, name))
				},
				navigateToMovieDetails = { id ->
					navigator.navigate(Destination.MovieDetails(id))
				},
				closeScreen = {
					navigator.goBack()
				}
			)
		}
		entry<Destination.Account> {
			setTitle(stringResource(com.zsoltbertalan.flickslate.shared.ui.R.string.app_name))
			val account by accountViewModel.loggedInEvent.collectAsStateWithLifecycle(null)
			AccountScreen(
				account = account,
				login = { username, password -> accountViewModel.login(username, password) },
				logout = { accountViewModel.logout() },
				navigateToMovieDetails = { id ->
					navigator.navigate(Destination.MovieDetails(id))
				},
				navigateToTvShowDetails = { id ->
					navigator.navigate(Destination.TvDetails(id))
				},
				navigateToTvSeasonDetails = { tvShowId, seasonNumber, episodeNumber ->
					navigator.navigate(
						Destination.TvDetails(
							tvShowId,
							seasonNumber,
							episodeNumber
						)
					)
				},
			)
		}
		entry<Destination.MovieDetails> { key ->
			MovieDetailScreen(
				movieId = key.movieId,
				setTitle = setTitle,
				setBackgroundColor = setBackgroundColor
			)
		}

		entry<Destination.TvDetails> { key ->
			TvDetailScreen(
				seriesId = key.seriesId,
				seasonNumber = key.seasonNumber,
				episodeNumber = key.episodeNumber,
				setTitle = setTitle,
				setBackgroundColor = setBackgroundColor,
				navigateToSeasonDetails = { tvShowId, seasonNumber, bgColor, bgColorDim, _ ->
					navigator.navigate(
						Destination.SeasonDetails(
							tvShowId,
							seasonNumber,
							bgColor.toArgb(),
							bgColorDim.toArgb()
						)
					)
				}
			)
		}

		entry<Destination.SeasonDetails> { key ->
			TvSeasonDetailScreen(
				seriesId = key.seriesId,
				seasonNumber = key.seasonNumber,
				bgColor = key.bgColor,
				bgColorDim = key.bgColorDim,
			)
		}

		entry<Destination.GenreMovies> { key ->
			GenreDetailScreen(
				genreId = key.genreId,
				genreName = key.genreName,
				setTitle = setTitle,
				setBackgroundColor = setBackgroundColor,
			) { id ->
				navigator.navigate(Destination.MovieDetails(id))
			}
		}
	}

	CompositionLocalProvider(LocalResultStore provides resultStore) {
		NavDisplay(
			entries = navigationState.toEntries(entryProvider),
			onBack = { navigator.goBack() },
			modifier = modifier.padding(paddingValues),
		)
	}

}

private val <KEY, T> PaginationInternalState<KEY, T>.isFirstPageCompleted: Boolean
	get() = when (this) {
		is PaginationInternalState.Initial -> false
		is PaginationInternalState.Loading -> items != null
		is PaginationInternalState.Loaded -> true
		is PaginationInternalState.Error -> true
	}
