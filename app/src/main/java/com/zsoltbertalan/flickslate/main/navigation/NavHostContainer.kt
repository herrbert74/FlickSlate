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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.zsoltbertalan.flickslate.account.ui.main.AccountScreen
import com.zsoltbertalan.flickslate.account.ui.main.AccountViewModel
import com.zsoltbertalan.flickslate.movies.ui.main.MoviesScreen
import com.zsoltbertalan.flickslate.movies.ui.main.MoviesViewModel
import com.zsoltbertalan.flickslate.movies.ui.moviedetails.MovieDetailScreen
import com.zsoltbertalan.flickslate.search.ui.main.GenreDetailScreen
import com.zsoltbertalan.flickslate.search.ui.main.GenreDetailViewModel
import com.zsoltbertalan.flickslate.search.ui.main.SearchScreen
import com.zsoltbertalan.flickslate.search.ui.main.SearchViewModel
import com.zsoltbertalan.flickslate.tv.ui.main.TvScreen
import com.zsoltbertalan.flickslate.tv.ui.main.TvViewModel
import com.zsoltbertalan.flickslate.tv.ui.seasondetail.TvSeasonDetailScreen
import com.zsoltbertalan.flickslate.tv.ui.tvdetail.TvDetailScreen
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun NavHostContainer(
	navController: NavHostController,
	paddingValues: PaddingValues,
	setTitle: (String) -> Unit,
	setBackgroundColor: (Color) -> Unit,
	modifier: Modifier = Modifier,
	viewModel: MoviesViewModel = hiltViewModel<MoviesViewModel>(),
	searchViewModel: SearchViewModel = hiltViewModel<SearchViewModel>(),
	tvViewModel: TvViewModel = hiltViewModel<TvViewModel>(),
	accountViewModel: AccountViewModel = hiltViewModel<AccountViewModel>(),
) {
	NavHost(
		navController = navController,
		startDestination = Destination.Movies,
		modifier = modifier.padding(paddingValues),
		builder = {
			composable<Destination.Movies> {
				setTitle(stringResource(com.zsoltbertalan.flickslate.shared.ui.R.string.app_name))
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
				setTitle(stringResource(com.zsoltbertalan.flickslate.shared.ui.R.string.app_name))
				val topRatedTv = tvViewModel.tvPaginationState
				TvScreen(paginatedState = topRatedTv) { id ->
					navController.navigate(Destination.TvDetails(id)) {
						popUpTo(Destination.Tv)
					}
				}
			}
			composable<Destination.Search> {
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
						navController.navigate(Destination.GenreMovies(id, name)) {
							popUpTo(Destination.Search)
						}
					},
					navigateToMovieDetails = { id ->
						navController.navigate(Destination.MovieDetails(id)) {
							popUpTo(Destination.Search)
						}
					},
					closeScreen = {
						navController.popBackStack()
					}
				)
			}
			composable<Destination.Account> {
				setTitle(stringResource(com.zsoltbertalan.flickslate.shared.ui.R.string.app_name))
				val account by accountViewModel.loggedInEvent.collectAsStateWithLifecycle(null)
				AccountScreen(
					account = account,
					login = { username, password -> accountViewModel.login(username, password) },
					logout = { accountViewModel.logout() }
				)
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
				MovieDetailScreen(setTitle = setTitle, setBackgroundColor = setBackgroundColor)
			}

			composable<Destination.TvDetails>(
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
				TvDetailScreen(
					setTitle = setTitle,
					setBackgroundColor = setBackgroundColor,
					navigateToSeasonDetails = { tvShowId, seasonNumber, bgColor, bgColorDim ->
						Timber.d("zsoltbertalan* NavHostContainer: $tvShowId, $seasonNumber")
						navController.navigate(
							Destination.SeasonDetails(
								tvShowId,
								seasonNumber,
								bgColor.toArgb(),
								bgColorDim.toArgb()
							)
						) {
							popUpTo(Destination.TvDetails(tvShowId))
						}
					}
				)
			}

			composable<Destination.SeasonDetails>(
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
				TvSeasonDetailScreen()
			}

			composable<Destination.GenreMovies> {
				val genreViewModel = hiltViewModel<GenreDetailViewModel>()
				val list = genreViewModel.genreMoviesPaginationState
				GenreDetailScreen(
					setTitle = setTitle,
					setBackgroundColor = setBackgroundColor,
					genreMoviesPaginatedState = list,
					genreName = genreViewModel.genreName,
				) { id ->
					navController.navigate(Destination.MovieDetails(id))
				}
			}
		}
	)

}

private fun tweenSpec(): TweenSpec<IntOffset> = tween(
	durationMillis = 500,
	easing = FastOutSlowInEasing
)
