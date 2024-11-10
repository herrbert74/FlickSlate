package com.zsoltbertalan.flickslate.main.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.zsoltbertalan.flickslate.movies.ui.main.MoviesScreen
import com.zsoltbertalan.flickslate.movies.ui.main.MoviesViewModel
import com.zsoltbertalan.flickslate.movies.ui.moviedetails.MovieDetailScreen
import com.zsoltbertalan.flickslate.search.ui.main.GenreDetailScreen
import com.zsoltbertalan.flickslate.search.ui.main.GenreDetailViewModel
import com.zsoltbertalan.flickslate.search.ui.main.SearchScreen
import com.zsoltbertalan.flickslate.search.ui.main.SearchViewModel
import com.zsoltbertalan.flickslate.tv.ui.main.TvScreen
import com.zsoltbertalan.flickslate.tv.ui.main.TvViewModel
import com.zsoltbertalan.flickslate.tv.ui.tvdetail.TvDetailScreen
import com.zsoltbertalan.flickslate.tv.ui.tvdetail.TvDetailViewModel
import kotlinx.coroutines.launch

@Composable
fun NavHostContainer(
	navController: NavHostController,
	paddingValues: PaddingValues,
	modifier: Modifier = Modifier,
	viewModel: MoviesViewModel = hiltViewModel<MoviesViewModel>(),
	searchViewModel: SearchViewModel = hiltViewModel<SearchViewModel>(),
	tvViewModel: TvViewModel = hiltViewModel<TvViewModel>(),
) {

	NavHost(
		navController = navController,
		startDestination = Destination.Movies,
		modifier = modifier.padding(paddingValues),
		builder = {
			composable<Destination.Movies> {
				val popularMovies = viewModel.popularMoviesPaginationState
				val nowPlaying = viewModel.nowPlayingMoviesPaginationState
				val upcoming = viewModel.upcomingMoviesPaginationState
				MoviesScreen(
					popularMoviesPaginatedState = popularMovies,
					nowPlayingMoviesPaginatedState = nowPlaying,
					upcomingMoviesPaginatedState = upcoming,
				) { id ->
					navController.navigate(Destination.MovieDetails(id)) {
						popUpTo(Destination.Movies)
					}
				}
			}
			composable<Destination.Tv> {
				val topRatedTv = tvViewModel.tvPaginationState
				TvScreen(paginatedState = topRatedTv) { id ->
					navController.navigate(Destination.TvDetails(id)) {
						popUpTo(Destination.Tv)
					}
				}
			}
			composable<Destination.Search> {
				val coroutineScope = rememberCoroutineScope()
				val searchState by searchViewModel.searchStateData.collectAsStateWithLifecycle()
				SearchScreen(
					searchState = searchState,
					navigateToGenreDetails = { id, name ->
						navController.navigate(Destination.GenreMovies(id, name)) {
							popUpTo(Destination.Search)
						}
					},
					navigateToMovieDetails = { id ->
						navController.navigate(Destination.MovieDetails(id)) {
							popUpTo(Destination.Search)
						}
					},
					searchEvent = {
						coroutineScope.launch {
							searchViewModel.emitEvent(it)
						}
					})
			}
			composable<Destination.MovieDetails>(
				enterTransition = {
					slideIntoContainer(
						AnimatedContentTransitionScope.SlideDirection.Down,
						animationSpec = tweenSpec()
					)
				},
				popExitTransition = {
					slideOutOfContainer(
						AnimatedContentTransitionScope.SlideDirection.Up,
						animationSpec = tweenSpec()
					)
				}
			) {
				MovieDetailScreen(popBackStack = { navController.popBackStack() })
			}

			composable<Destination.TvDetails> {
				val tvDetailViewModel = hiltViewModel<TvDetailViewModel>()
				val detail = tvDetailViewModel.tvStateData.collectAsStateWithLifecycle()
				TvDetailScreen(detail = detail) { navController.popBackStack() }
			}

			composable<Destination.GenreMovies> {
				val genreViewModel = hiltViewModel<GenreDetailViewModel>()
				val list = genreViewModel.genreMoviesPaginationState
				GenreDetailScreen(
					genreMoviesPaginatedState = list,
					genreName = genreViewModel.genreName,
					popBackStack = { navController.popBackStack() }
				) { id ->
					navController.navigate("detail/${id}")
				}
			}
		})

}

private fun tweenSpec(): TweenSpec<IntOffset> = tween(
	durationMillis = 500,
	easing = FastOutSlowInEasing
)
