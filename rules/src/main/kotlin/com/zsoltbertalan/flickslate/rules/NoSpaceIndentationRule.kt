package com.zsoltbertalan.flickslate.rules

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import io.gitlab.arturbosch.detekt.api.config
import org.jetbrains.kotlin.psi.KtFile

private const val DEFAULT_TAB_WIDTH = 4

class NoSpaceIndentationRule(config: Config = Config.empty) : Rule(config) {

	override val issue = Issue(
		id = "NoSpaceIndentation",
		severity = Severity.Style,
		description = "Use tabs for indentation instead of spaces.",
		debt = Debt.FIVE_MINS
	)

	private val tabWidth: Int by config(DEFAULT_TAB_WIDTH)

	override fun visitKtFile(file: KtFile) {
		super.visitKtFile(file)
		val text = file.text
		val lines = text.split("\n")
		var offset = 0
		var lineNumber = 1
		val effectiveTabWidth = if (tabWidth < 2) 2 else tabWidth
		val spacePrefix = " ".repeat(effectiveTabWidth)

		lines.forEach { line ->
			if (line.startsWith(spacePrefix)) {
				report(
					CodeSmell(
						issue = issue,
						entity = Entity.from(file, offset),
						message = "Line $lineNumber uses spaces for indentation. Use tabs instead."
					)
				)
			}
			offset += line.length + 1
			lineNumber++
		}
	}

}
