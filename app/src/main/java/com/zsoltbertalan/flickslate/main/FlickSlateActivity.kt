package com.zsoltbertalan.flickslate.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.zsoltbertalan.flickslate.main.navigation.Destination
import com.zsoltbertalan.flickslate.main.navigation.NavHostContainer
import com.zsoltbertalan.flickslate.shared.async.IoDispatcher
import com.zsoltbertalan.flickslate.shared.async.MainDispatcher
import com.zsoltbertalan.flickslate.shared.compose.component.FlickSlateTopAppBar
import com.zsoltbertalan.flickslate.shared.compose.design.FlickSlateTheme
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
				val navController = rememberNavController()
				val bottomBarState = rememberSaveable { (mutableStateOf(true)) }
				val navBackStackEntry by navController.currentBackStackEntryAsState()
				val currentDestination = navBackStackEntry?.destination.getDestination()

				when (currentDestination) {
					Destination.MovieDetails::class,
					Destination.TvDetails::class,
					Destination.GenreMovies::class
						-> bottomBarState.value = false

					else
						-> if (!bottomBarState.value) bottomBarState.value = true

				}
				Scaffold(
					modifier = Modifier.fillMaxSize(),
					topBar = {
						if (bottomBarState.value) {
							FlickSlateTopAppBar()
						}
					},
					bottomBar = {
						if (bottomBarState.value) {
							FlickSlateBottomNavigationBar(
								navController = navController,
							)
						}
					}
				) { paddingValues ->
					NavHostContainer(navController = navController, paddingValues = paddingValues)
				}
			}
		}
	}

}

private fun NavDestination?.getDestination(): KClass<out Destination> =
	when (true) {
		this?.hasRoute<Destination.Movies>() -> Destination.Movies::class
		this?.hasRoute<Destination.Tv>() -> Destination.Tv::class
		this?.hasRoute<Destination.GenreMovies>() -> Destination.GenreMovies::class
		this?.hasRoute<Destination.MovieDetails>() -> Destination.MovieDetails::class
		this?.hasRoute<Destination.TvDetails>() -> Destination.TvDetails::class
		this?.hasRoute<Destination.Search>() -> Destination.Search::class
		this?.hasRoute<Destination.Account>() -> Destination.Account::class
		else -> Destination.Movies::class
	}
