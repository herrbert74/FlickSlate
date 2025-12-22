package com.zsoltbertalan.flickslate.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.zsoltbertalan.flickslate.main.navigation.Destination
import com.zsoltbertalan.flickslate.main.navigation.NavHostContainer
import com.zsoltbertalan.flickslate.main.navigation.Navigator
import com.zsoltbertalan.flickslate.main.navigation.rememberNavigationState
import com.zsoltbertalan.flickslate.shared.kotlin.async.IoDispatcher
import com.zsoltbertalan.flickslate.shared.kotlin.async.MainDispatcher
import com.zsoltbertalan.flickslate.shared.ui.R
import com.zsoltbertalan.flickslate.shared.ui.compose.component.FlickSlateTopAppBar
import com.zsoltbertalan.flickslate.shared.ui.compose.design.Colors
import com.zsoltbertalan.flickslate.shared.ui.compose.design.FlickSlateTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@AndroidEntryPoint
class FlickSlateActivity : ComponentActivity() {

	@Inject
	@MainDispatcher
	lateinit var mainContext: CoroutineDispatcher

	@Inject
	@IoDispatcher
	lateinit var ioContext: CoroutineDispatcher

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			FlickSlateTheme {
				val bg = Colors.surface
				val (title, setTitle) = remember { mutableStateOf(getString(R.string.app_name)) }
				val (showBack, setShowBack) = remember { mutableStateOf(false) }
				val (backgroundColor, setBackgroundColor) = remember { mutableStateOf(bg) }
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
						setTitle = setTitle,
						setBackgroundColor = setBackgroundColor,
					)
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

}
