package com.zsoltbertalan.flickslate.rules

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.psi.KtElement
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.psiUtil.elementsInRange
import org.jetbrains.kotlin.psi.psiUtil.getNonStrictParentOfType

/**
 * Util function to search for the [KtElement]s in the parents of
 * the given [line] from a given offset in a [KtFile].
 */
internal fun findKtElementInParents(file: KtFile, offset: Int, line: String): Sequence<PsiElement> {
	val start = (offset - line.length).coerceAtLeast(0)
	val end = offset.coerceAtMost(file.textLength)
	if (start >= end) {
		return emptySequence()
	}
	return try {
		file.elementsInRange(TextRange.create(start, end))
			.asSequence()
			.plus(file.findElementAt(end.coerceAtMost((file.textLength - 1).coerceAtLeast(0))))
			.mapNotNull { it?.getNonStrictParentOfType() }
	} catch (_: IndexOutOfBoundsException) {
		emptySequence()
	}
}
