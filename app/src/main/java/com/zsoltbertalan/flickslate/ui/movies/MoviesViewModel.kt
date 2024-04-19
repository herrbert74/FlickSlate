package com.zsoltbertalan.flickslate.ui.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.zsoltbertalan.flickslate.domain.api.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(moviesRepository: MoviesRepository) : ViewModel() {

	val popularMoviesList = moviesRepository.getPopularMovies("en").cachedIn(viewModelScope)
	val nowPlayingMoviesList = moviesRepository.getNowPlayingMovies("en").cachedIn(viewModelScope)
	val upcomingMoviesList = moviesRepository.getUpcomingMovies("en").cachedIn(viewModelScope)

}
