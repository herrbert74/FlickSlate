package com.zsoltbertalan.flickslate.rules

import org.jetbrains.kotlin.psi.KtFile

internal data class KtFileContent(val file: KtFile, val content: Sequence<String>)

internal fun KtFile.toFileContent() = KtFileContent(this, text.splitToSequence("\n"))
