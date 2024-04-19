package com.zsoltbertalan.flickslate.design

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

const val SMALLEST_WIDTH_480 = 480
const val SMALLEST_WIDTH_600 = 600
const val SMALLEST_WIDTH_720 = 720

@Suppress("unused", "LongParameterList")
class Dimensions(
    val marginSmallest: Dp,
    val marginExtraSmall: Dp,
    val marginSmall: Dp,
    val marginNormal: Dp,
    val marginLarge: Dp,
    val marginExtraLarge: Dp,
    val marginDoubleLarge: Dp,
    val marginTripleLarge: Dp,

    val buttonWidth: Dp,
    val buttonWidthShort: Dp,
    val buttonWidthFitTwo: Dp,
    val buttonHeight: Dp,
    val buttonSmallHeight: Dp,

    val listSingleItemHeight: Dp,
    val listSingleItemDenseHeight: Dp,
    val listSingleItemLargeHeight: Dp,
    val listTwoLineHeight: Dp,
    val listTwoLineDenseHeight: Dp,
    val listThreeLineHeight: Dp,
    val listAvatarWidth: Dp,
    val listSubheadingHeight: Dp,

    val fabSize: Dp,
    val iconSize: Dp,
    val appBarHeight: Dp,
)

val smallDimensions = Dimensions(
    marginSmallest = 1.dp,
    marginExtraSmall = 2.dp,
    marginSmall = 4.dp,
    marginNormal = 8.dp,
    marginLarge = 16.dp,
    marginExtraLarge = 24.dp,
    marginDoubleLarge = 32.dp,
    marginTripleLarge = 64.dp,

    buttonWidth = 200.dp,
    buttonWidthShort = 128.dp,
    buttonWidthFitTwo = 96.dp,
    buttonHeight = 42.dp,
    buttonSmallHeight = 40.dp,

    listSingleItemHeight = 56.dp,
    listSingleItemDenseHeight = 48.dp,
    listSingleItemLargeHeight = 72.dp,
    listTwoLineHeight = 72.dp,
    listTwoLineDenseHeight = 64.dp,
    listThreeLineHeight = 88.dp,
    listAvatarWidth = 40.dp,
    listSubheadingHeight = 48.dp,

    fabSize = 56.dp,
    iconSize = 24.dp,
    appBarHeight = 216.dp,
)

val sw600Dimensions = Dimensions(
    marginSmallest = 1.dp,
    marginExtraSmall = 3.dp,
    marginSmall = 6.dp,
    marginNormal = 12.dp,
    marginLarge = 24.dp,
    marginExtraLarge = 36.dp,
    marginDoubleLarge = 72.dp,
    marginTripleLarge = 96.dp,

    buttonWidth = 300.dp,
    buttonWidthShort = 172.dp,
    buttonWidthFitTwo = 144.dp,
    buttonHeight = 63.dp,
    buttonSmallHeight = 60.dp,

    listSingleItemHeight = 84.dp,
    listSingleItemDenseHeight = 72.dp,
    listSingleItemLargeHeight = 108.dp,
    listTwoLineHeight = 108.dp,
    listTwoLineDenseHeight = 96.dp,
    listThreeLineHeight = 132.dp,
    listAvatarWidth = 60.dp,
    listSubheadingHeight = 72.dp,

    fabSize = 84.dp,
    iconSize = 36.dp,
    appBarHeight = 324.dp,
)
