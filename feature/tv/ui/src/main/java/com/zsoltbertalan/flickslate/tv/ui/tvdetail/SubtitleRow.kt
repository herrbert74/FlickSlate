package com.zsoltbertalan.flickslate.tv.ui.tvdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zsoltbertalan.flickslate.shared.domain.model.Genre
import com.zsoltbertalan.flickslate.shared.ui.compose.design.Colors
import com.zsoltbertalan.flickslate.shared.ui.compose.design.Dimens
import com.zsoltbertalan.flickslate.shared.ui.compose.design.FlickSlateTheme
import com.zsoltbertalan.flickslate.tv.domain.model.TvDetailWithImages
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun SubtitleRow(tvDetail: TvDetailWithImages, modifier: Modifier = Modifier) {
	LazyRow(
		modifier
			.fillMaxWidth(1f)
			.wrapContentHeight(align = Alignment.Top)
			.height(Dimens.listSingleItemHeight),
		horizontalArrangement = Arrangement.Start,
	) {
		item {
			Text(
				modifier = Modifier.padding(16.dp),
				text = tvDetail.tagline ?: tvDetail.title ?: ""
			)
		}
		item {
			VerticalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Colors.onSurface)
		}
		item {
			Text(
				modifier = Modifier.padding(16.dp),
				text = tvDetail.status.toString()
			)
		}
		item {
			VerticalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Colors.onSurface)
		}
		item {
			Text(
				modifier = Modifier.padding(16.dp),
				text = tvDetail.voteAverage.toString()
			)
		}
		item {
			VerticalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Colors.onSurface)
		}
		item {
			Text(
				modifier = Modifier.padding(16.dp),
				text = tvDetail.years
			)
		}
	}
}

@Preview
@Composable
internal fun SubtitleRowPreview() {
	FlickSlateTheme {
		SubtitleRow(
			modifier = Modifier,
			tvDetail = TvDetailWithImages(
				backdropPath = "",
				genres = listOf<Genre>().toImmutableList(),
				id = 123,
				status = "Ended",
				tagline = "Change the equation.",
				title = "Title",
				voteAverage = 8.04f,
				years = "2011-2019",
			)
		)
	}

}
