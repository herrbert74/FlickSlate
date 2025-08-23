package com.zsoltbertalan.flickslate.movies.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest

class MoviesScreenScreenshots {

	@PreviewTest
	@Preview(showBackground = true)
	@Composable
	private fun TopAppBarPreviewTest() {
		ShowNowPlayingMoviesPreview()
	}

}
