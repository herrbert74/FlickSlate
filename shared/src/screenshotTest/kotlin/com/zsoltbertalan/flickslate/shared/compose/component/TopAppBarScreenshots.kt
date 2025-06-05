package com.zsoltbertalan.flickslate.shared.compose.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest

class TopAppBarScreenshots {

	@PreviewTest
	@Preview(showBackground = true)
	@Composable
	private fun TopAppBarPreviewTest() {
		TopAppBarPreview()
	}

	@PreviewTest
	@Preview(showBackground = true)
	@Composable
	private fun TopAppBarPreviewShowBackTest() {
		TopAppBarPreviewShowBack()
	}

}
