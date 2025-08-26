package com.zsoltbertalan.flickslate.tv.ui.tvdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zsoltbertalan.flickslate.shared.kotlin.result.Failure
import com.zsoltbertalan.flickslate.tv.domain.model.TvDetailWithImages
import com.zsoltbertalan.flickslate.tv.domain.usecase.TvDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

const val SERIES_ID_ARG = "seriesId"

@HiltViewModel
class TvDetailViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,
	private val tvDetailsUseCase: TvDetailsUseCase
) : ViewModel() {

	private val seriesId: Int = checkNotNull(savedStateHandle[SERIES_ID_ARG])

	private val _tvStateData = MutableStateFlow(TvDetailState())
	val tvStateData = _tvStateData.asStateFlow()

	init {
		getTvDetail()
	}

	private fun getTvDetail() {
		viewModelScope.launch {
			val tvDetailsResult = tvDetailsUseCase.getTvDetails(seriesId)
			when {
				tvDetailsResult.isOk -> _tvStateData.update {
					it.copy(
						tvDetail = tvDetailsResult.value,
						failure = null
					)
				}

				else -> _tvStateData.update {
					it.copy(
						tvDetail = null,
						failure = tvDetailsResult.error
					)
				}
			}

		}
	}
}

data class TvDetailState(
	val tvDetail: TvDetailWithImages? = null,
	val failure: Failure? = null,
)
