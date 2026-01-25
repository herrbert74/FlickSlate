package com.zsoltbertalan.flickslate.main

import android.os.Bundle
import android.os.SystemClock
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.zsoltbertalan.flickslate.main.navigation.Destination
import com.zsoltbertalan.flickslate.main.navigation.NavHostContainer
import com.zsoltbertalan.flickslate.main.navigation.Navigator
import com.zsoltbertalan.flickslate.main.navigation.rememberNavigationState
import com.zsoltbertalan.flickslate.base.kotlin.async.IoDispatcher
import com.zsoltbertalan.flickslate.base.kotlin.async.MainDispatcher
import com.zsoltbertalan.flickslate.shared.ui.R
import com.zsoltbertalan.flickslate.shared.ui.compose.component.FlickSlateTopAppBar
import com.zsoltbertalan.flickslate.shared.ui.compose.design.Colors
import com.zsoltbertalan.flickslate.shared.ui.compose.design.FlickSlateTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import javax.inject.Inject

private const val MINIMUM_LAUNCH_ANIMATION_DURATION = 1000L
private const val EXIT_ANIMATION_DURATION = 1200L
private const val COMPOSE_VIEW_FADE_DURATION_OFFSET = 600L

@AndroidEntryPoint
class FlickSlateActivity : ComponentActivity() {

	@Inject
	@MainDispatcher
	lateinit var mainContext: CoroutineDispatcher

	@Inject
	@IoDispatcher
	lateinit var ioContext: CoroutineDispatcher

	var splashDecorator: SplashScreenDecorator? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		val isRunningTest = intent.extras?.getBoolean("isRunningTest") ?: false
		if (isRunningTest) {
			installSplashScreen()
		} else {
			splashDecorator = splash {
				exitAnimationDuration = EXIT_ANIMATION_DURATION
				composeViewFadeDurationOffset = COMPOSE_VIEW_FADE_DURATION_OFFSET
				content {
					LaunchedEffect(isVisible.value) {
						if (!isVisible.value) startExitAnimation()
					}

					FlickSlateTheme {
						Box(
							modifier = Modifier
								.fillMaxSize()
								.background(Colors.surface),
							contentAlignment = Alignment.Center
						) {
							ClapperboardTransition(
								isVisible = isVisible.value,
								modifier = Modifier.size(288.dp)
							)
						}
					}
				}
			}
		}

		super.onCreate(savedInstanceState)
		setContent {
			FlickSlateTheme {
				val bg = Colors.surfaceDim
				val (title, setTitle) = remember { mutableStateOf(getString(R.string.app_name)) }
				val (showBack, setShowBack) = remember { mutableStateOf(false) }
				val (backgroundColor, setBackgroundColor) = remember { mutableStateOf(bg) }
				val (moviesListsReady, setMoviesListsReady) = remember { mutableStateOf(false) }
				val (splashTransitionStartTimeMillis, setSplashTransitionStartTimeMillis) = remember {
					mutableStateOf<Long?>(
						null
					)
				}
				val (hasDismissedSplash, setHasDismissedSplash) = rememberSaveable { mutableStateOf(false) }

				LaunchedEffect(Unit) {
					withFrameNanos { }
					if (!isRunningTest) {
						splashDecorator?.shouldKeepOnScreen = false
						setSplashTransitionStartTimeMillis(SystemClock.uptimeMillis())
					}
				}

				LaunchedEffect(moviesListsReady, splashTransitionStartTimeMillis) {
					val start = splashTransitionStartTimeMillis ?: return@LaunchedEffect
					if (!moviesListsReady) return@LaunchedEffect
					if (hasDismissedSplash) return@LaunchedEffect
					val elapsed = SystemClock.uptimeMillis() - start
					delay((MINIMUM_LAUNCH_ANIMATION_DURATION - elapsed).coerceAtLeast(0L))
					splashDecorator?.dismiss()
					setHasDismissedSplash(true)
				}
				val navigationState = rememberNavigationState(
					startRoute = Destination.Movies,
					topLevelRoutes = persistentSetOf(
						Destination.Movies,
						Destination.Tv,
						Destination.Search,
						Destination.Account
					)
				)
				val navigator = remember { Navigator(navigationState) }
				val (isBottomBarVisible, setBottomBarVisible) = rememberSaveable { (mutableStateOf(true)) }
				val currentStack = navigationState.backStacks[navigationState.topLevelRoute]
				val currentRoute = currentStack?.lastOrNull() ?: navigationState.topLevelRoute

				when (currentRoute) {
					is Destination.MovieDetails,
					is Destination.TvDetails,
					is Destination.SeasonDetails,
					is Destination.GenreMovies -> SetLowLevelScaffoldParams(
						setShowBack,
						isBottomBarVisible,
						setBottomBarVisible
					)

					else -> SetTopLevelScaffoldParams(
						setShowBack,
						setBackgroundColor,
						isBottomBarVisible,
						setBottomBarVisible
					)

				}
				Box(modifier = Modifier.fillMaxSize()) {
					Scaffold(
						modifier = Modifier.fillMaxSize(),
						topBar = {
							FlickSlateTopAppBar(
								title = title,
								showBack = showBack,
								backgroundColor = backgroundColor,
								popBackStack = {
									navigator.goBack()
									true
								}
							)
						},
						bottomBar = {
							if (isBottomBarVisible) {
								FlickSlateBottomNavigationBar(
									navigationState = navigationState,
									navigator = navigator,
								)
							}
						}
					) { paddingValues ->
						NavHostContainer(
							navigationState = navigationState,
							navigator = navigator,
							paddingValues = paddingValues,
							setMoviesListsReady = setMoviesListsReady,
							setTitle = setTitle,
							setBackgroundColor = setBackgroundColor,
						)
					}
				}
			}
		}
	}

	@Composable
	private fun SetLowLevelScaffoldParams(
		setShowBack: (Boolean) -> Unit,
		isBottomBarVisible: Boolean,
		setBottomBarVisible: (Boolean) -> Unit
	) {
		setShowBack(true)
		if (isBottomBarVisible) setBottomBarVisible(false)
	}

	@Composable
	private fun SetTopLevelScaffoldParams(
		setShowBack: (Boolean) -> Unit,
		setBackgroundColor: (Color) -> Unit,
		isBottomBarVisible: Boolean,
		setBottomBarVisible: (Boolean) -> Unit,
	) {
		setShowBack(false)
		setBackgroundColor(Colors.surface)
		if (!isBottomBarVisible) setBottomBarVisible(true)
	}

	override fun onDestroy() {
		splashDecorator = null
		super.onDestroy()
	}

}
