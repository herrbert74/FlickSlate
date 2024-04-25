package com.zsoltbertalan.flickslate.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.zsoltbertalan.flickslate.design.FlickSlateTheme
import com.zsoltbertalan.flickslate.di.IoDispatcher
import com.zsoltbertalan.flickslate.di.MainDispatcher
import com.zsoltbertalan.flickslate.domain.api.MoviesRepository
import com.zsoltbertalan.flickslate.ui.component.FlickSlateBottomNavigationBar
import com.zsoltbertalan.flickslate.ui.component.FlickSlateTopAppBar
import com.zsoltbertalan.flickslate.ui.navigation.GENRE_DETAIL_ROUTE
import com.zsoltbertalan.flickslate.ui.navigation.MOVIE_DETAIL_ROUTE
import com.zsoltbertalan.flickslate.ui.navigation.NavHostContainer
import com.zsoltbertalan.flickslate.ui.navigation.TV_DETAIL_ROUTE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@AndroidEntryPoint
class FlickSlateActivity : ComponentActivity() {

	@Inject
	lateinit var moviesRepository: MoviesRepository

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
				when (navBackStackEntry?.destination?.route) {
					MOVIE_DETAIL_ROUTE, TV_DETAIL_ROUTE, GENRE_DETAIL_ROUTE -> bottomBarState.value =
						false

					else -> {
						if (!bottomBarState.value) bottomBarState.value = true
					}
				}
				Scaffold(
					modifier = Modifier.fillMaxSize(),
					topBar = { if (bottomBarState.value) FlickSlateTopAppBar() },
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
