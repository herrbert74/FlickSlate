package com.zsoltbertalan.flickslate.account.ui.ratings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zsoltbertalan.flickslate.account.domain.usecase.GetRatedMoviesUseCase
import com.zsoltbertalan.flickslate.account.domain.usecase.GetRatedTvShowEpisodesUseCase
import com.zsoltbertalan.flickslate.account.domain.usecase.GetRatedTvShowsUseCase
import com.zsoltbertalan.flickslate.shared.domain.model.Movie
import com.zsoltbertalan.flickslate.shared.domain.model.TvEpisodeDetail
import com.zsoltbertalan.flickslate.shared.domain.model.TvShow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RatingsViewModel @Inject constructor(
    private val getRatedMoviesUseCase: GetRatedMoviesUseCase,
    private val getRatedTvShowsUseCase: GetRatedTvShowsUseCase,
    private val getRatedTvShowEpisodesUseCase: GetRatedTvShowEpisodesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<RatingsUiState>(RatingsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        fetchRatings()
    }

    private fun fetchRatings() {
        viewModelScope.launch {
            _uiState.value = RatingsUiState.Loading
            val moviesResult = getRatedMoviesUseCase.execute()
            val tvShowsResult = getRatedTvShowsUseCase.execute()
            val tvEpisodesResult = getRatedTvShowEpisodesUseCase.execute()

            if (moviesResult.isOk && tvShowsResult.isOk && tvEpisodesResult.isOk) {
                _uiState.value = RatingsUiState.Success(
                    ratedMovies = moviesResult.value.toImmutableList(),
                    ratedTvShows = tvShowsResult.value.toImmutableList(),
                    ratedTvEpisodes = tvEpisodesResult.value.toImmutableList(),
                )
            } else {
                 _uiState.value = RatingsUiState.Error("Failed to load ratings.")
            }
        }
    }
}

sealed class RatingsUiState {
    data object Loading : RatingsUiState()
    data class Success(
		val ratedMovies: ImmutableList<Movie> = listOf<Movie>().toImmutableList(),
		val ratedTvShows: ImmutableList<TvShow> = listOf<TvShow>().toImmutableList(),
		val ratedTvEpisodes: ImmutableList<TvEpisodeDetail> = listOf<TvEpisodeDetail>().toImmutableList()
    ) : RatingsUiState()
    data class Error(val message: String) : RatingsUiState()
}
