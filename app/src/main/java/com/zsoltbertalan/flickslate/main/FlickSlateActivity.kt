package com.zsoltbertalan.flickslate.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.zsoltbertalan.flickslate.main.navigation.Destination
import com.zsoltbertalan.flickslate.main.navigation.NavHostContainer
import com.zsoltbertalan.flickslate.shared.kotlin.async.IoDispatcher
import com.zsoltbertalan.flickslate.shared.kotlin.async.MainDispatcher
import com.zsoltbertalan.flickslate.shared.ui.R
import com.zsoltbertalan.flickslate.shared.ui.compose.component.FlickSlateTopAppBar
import com.zsoltbertalan.flickslate.shared.ui.compose.design.Colors
import com.zsoltbertalan.flickslate.shared.ui.compose.design.FlickSlateTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import kotlin.reflect.KClass

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
				val navController = rememberNavController()
				val (isBottomBarVisible, setBottomBarVisible) = rememberSaveable { (mutableStateOf(true)) }
				val navBackStackEntry by navController.currentBackStackEntryAsState()
				val currentDestination = navBackStackEntry?.destination.getDestination()

				when (currentDestination) {
					Destination.MovieDetails::class,
					Destination.TvDetails::class,
					Destination.SeasonDetails::class,
					Destination.GenreMovies::class -> SetLowLevelScaffoldParams(
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
							popBackStack = { navController.popBackStack() }
						)
					},
					bottomBar = {
						if (isBottomBarVisible) {
							FlickSlateBottomNavigationBar(
								navController = navController,
							)
						}
					}
				) { paddingValues ->
					NavHostContainer(
						navController = navController,
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

private fun NavDestination?.getDestination(): KClass<out Destination> =
	when (true) {
		this?.hasRoute<Destination.Movies>() -> Destination.Movies::class
		(this == null) -> Destination.Movies::class
		this?.hasRoute<Destination.Tv>() -> Destination.Tv::class
		this?.hasRoute<Destination.GenreMovies>() -> Destination.GenreMovies::class
		this?.hasRoute<Destination.MovieDetails>() -> Destination.MovieDetails::class
		this?.hasRoute<Destination.TvDetails>() -> Destination.TvDetails::class
		this?.hasRoute<Destination.SeasonDetails>() -> Destination.SeasonDetails::class
		this?.hasRoute<Destination.Search>() -> Destination.Search::class
		this?.hasRoute<Destination.Account>() -> Destination.Account::class
		else -> error("Unknown destination: $this")
	}
