package com.zsoltbertalan.flickslate.ui.tv

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.zsoltbertalan.flickslate.domain.model.Tv
import com.zsoltbertalan.flickslate.R
import com.zsoltbertalan.flickslate.design.Colors
import com.zsoltbertalan.flickslate.domain.model.MovieCardType
import com.zsoltbertalan.flickslate.ext.navigate
import com.zsoltbertalan.flickslate.ui.component.ListTitle
import com.zsoltbertalan.flickslate.ui.component.ShowLoading
import com.zsoltbertalan.flickslate.ui.component.ShowCard

@Composable
fun TvScreen(
	modifier: Modifier = Modifier,
	topRatedTv: LazyPagingItems<Tv>,
	popTo: (Int) -> Unit,
) {
	LazyColumn(
		modifier = modifier
			.background(Colors.background)
			.fillMaxHeight()
	) {
		showTopRatedTv(topRatedTv, popTo)
	}
}

private fun LazyListScope.showTopRatedTv(
	topRatedTv: LazyPagingItems<Tv>,
	popTo: (Int) -> Unit,
) {

	item {
		ListTitle(titleId = R.string.top_rated_tv)
	}

	items(topRatedTv.itemCount) { index ->
		topRatedTv[index].let {
			it?.let {
				ShowCard(
					modifier = Modifier.navigate(it.id, popTo),
					title = it.name,
					voteAverage = it.voteAverage,
					overview = it.overview,
					posterPath = it.posterPath,
					cardType = MovieCardType.FULL
				)
			}

		}
	}

	item {
		Spacer(modifier = Modifier.height(20.dp))
	}

	when {
		topRatedTv.loadState.refresh is LoadState.Loading -> {
			item {
				ShowLoading(
					text = stringResource(id = R.string.top_rated_tv)
				)
			}
		}

		topRatedTv.loadState.append is LoadState.Loading -> {
			item {
				ShowLoading(
					text = stringResource(id = R.string.top_rated_tv)
				)
			}
		}
	}
}
