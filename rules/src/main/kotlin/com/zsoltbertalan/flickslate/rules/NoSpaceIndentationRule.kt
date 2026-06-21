package com.zsoltbertalan.flickslate.rules

import com.intellij.openapi.util.TextRange
import dev.detekt.api.Config
import dev.detekt.api.Entity
import dev.detekt.api.Finding
import dev.detekt.api.Location
import dev.detekt.api.Rule
import dev.detekt.api.SourceLocation
import dev.detekt.api.TextLocation
import dev.detekt.api.config
import dev.detekt.psi.absolutePath
import org.jetbrains.kotlin.KtPsiSourceFileLinesMapping
import org.jetbrains.kotlin.diagnostics.DiagnosticUtils.getLineAndColumnRangeInPsiFile
import org.jetbrains.kotlin.psi.KtFile

private const val DEFAULT_TAB_WIDTH = 4

class NoSpaceIndentationRule(config: Config = Config.empty) : Rule(config, "Use tabs for indentation instead of spaces.") {

	private val tabWidth: Int by config(DEFAULT_TAB_WIDTH)

	override fun visitKtFile(file: KtFile) {
		super.visitKtFile(file)
		val sourceFileLinesMapping = KtPsiSourceFileLinesMapping(file)
		val effectiveTabWidth = if (tabWidth < 2) 2 else tabWidth
		val spacePrefix = " ".repeat(effectiveTabWidth)

		file.text.lineSequence().withIndex()
			.filter { (index, _) -> index < sourceFileLinesMapping.linesCount }
			.forEach { (index, line) ->
				if (!line.startsWith(spacePrefix)) {
					return@forEach
				}

				val offset = sourceFileLinesMapping.getLineStartOffset(index)
				val textRange = TextRange(offset, offset + line.length)
				val lineAndColumnRange = getLineAndColumnRangeInPsiFile(file, textRange)
				val location = Location(
					source = SourceLocation(lineAndColumnRange.start.line, lineAndColumnRange.start.column),
					endSource = SourceLocation(lineAndColumnRange.end.line, lineAndColumnRange.end.column),
					text = TextLocation(offset, offset + line.length),
					path = file.absolutePath(),
				)
				report(
					Finding(
						entity = Entity.from(file, location),
						message = "Line ${index + 1} uses spaces for indentation. Use tabs instead.",
					)
				)
			}
	}

}
