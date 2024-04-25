package com.zsoltbertalan.flickslate.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.paging.compose.collectAsLazyPagingItems
import com.zsoltbertalan.flickslate.ui.moviedetails.MovieDetailScreen
import com.zsoltbertalan.flickslate.ui.movies.MoviesScreen
import com.zsoltbertalan.flickslate.ui.movies.MoviesViewModel
import com.zsoltbertalan.flickslate.ui.search.GenreDetailScreen
import com.zsoltbertalan.flickslate.ui.search.GenreDetailViewModel
import com.zsoltbertalan.flickslate.ui.search.SearchScreen
import com.zsoltbertalan.flickslate.ui.search.SearchViewModel
import com.zsoltbertalan.flickslate.ui.tv.TvScreen
import com.zsoltbertalan.flickslate.ui.tv.TvViewModel
import com.zsoltbertalan.flickslate.ui.tvdetail.TvDetailScreen
import com.zsoltbertalan.flickslate.ui.tvdetail.TvDetailViewModel
import kotlinx.coroutines.launch

const val MOVIE_DETAIL_ROUTE = "detail/{movieId}"
const val TV_DETAIL_ROUTE = "tvDetail/{seriesId}"
const val GENRE_DETAIL_ROUTE = "genreDetail/{genreId}/{genreName}"

@Composable
fun NavHostContainer(
	navController: NavHostController,
	paddingValues: PaddingValues,
) {
	val viewModel = hiltViewModel<MoviesViewModel>()
	val searchViewModel = hiltViewModel<SearchViewModel>()
	val tvViewModel = hiltViewModel<TvViewModel>()

	NavHost(
		navController = navController,
		startDestination = Destination.MOVIE.route,
		modifier = Modifier.padding(paddingValues),
		builder = {
			composable(Destination.MOVIE.route) {
				val popularMovies = viewModel.popularMoviesList.collectAsLazyPagingItems()
				val nowPlaying = viewModel.nowPlayingMoviesList.collectAsLazyPagingItems()
				val upcoming = viewModel.upcomingMoviesList.collectAsLazyPagingItems()
				MoviesScreen(
					popularMovies = popularMovies,
					nowPlaying = nowPlaying,
					upcoming = upcoming,
				) { id ->
					navController.navigate("detail/${id}") {
						popUpTo("movies")
					}
				}
			}
			composable(Destination.TV.route) {
				val topRatedTv = tvViewModel.topRatedTvList.collectAsLazyPagingItems()
				TvScreen(topRatedTv = topRatedTv) { id ->
					navController.navigate("tvDetail/${id}") {
						popUpTo("tv")
					}
				}
			}
			composable(Destination.SEARCH.route) {
				val coroutineScope = rememberCoroutineScope()
				val searchState by searchViewModel.searchStateData.collectAsStateWithLifecycle()
				SearchScreen(
					searchState = searchState,
					navigateToGenreDetails = { id, name ->
						navController.navigate("genreDetail/${id}/${name}") {
							popUpTo("search")
						}
					},
					navigateToMovieDetails = { id ->
						navController.navigate("detail/${id}") {
							popUpTo("movies")
						}
					},
					searchEvent = {
						coroutineScope.launch {
							searchViewModel.emitEvent(it)
						}
					})
			}
			composable(
				MOVIE_DETAIL_ROUTE,
				arguments = listOf(navArgument("movieId") { type = NavType.IntType }),
				enterTransition = {
					slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Down)
				},
				popExitTransition = {
					slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Up)
				}
			) {
				MovieDetailScreen(popBackStack = { navController.popBackStack() })
			}

			composable(
				TV_DETAIL_ROUTE,
				arguments = listOf(navArgument("seriesId") { type = NavType.IntType })
			) {
				val tvDetailViewModel = hiltViewModel<TvDetailViewModel>()
				val detail = tvDetailViewModel.tvStateData.collectAsStateWithLifecycle()
				TvDetailScreen(detail = detail) { navController.popBackStack() }
			}

			composable(
				GENRE_DETAIL_ROUTE,
				arguments = listOf(navArgument("genreId") { type = NavType.IntType },
					navArgument("genreName") { type = NavType.StringType }),
				enterTransition = {
					slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up)
				},
				exitTransition = {
					slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down)
				},
				popEnterTransition = {
					slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up)
				},
				popExitTransition = {
					slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down)
				}
			) {
				val genreViewModel = hiltViewModel<GenreDetailViewModel>()
				val list = genreViewModel.genreDetailList.collectAsLazyPagingItems()
				GenreDetailScreen(
					genreDetailList = list,
					genreName = genreViewModel.genreName,
					popBackStack = { navController.popBackStack() }
				) { id ->
					navController.navigate("detail/${id}")
				}
			}
		})
}