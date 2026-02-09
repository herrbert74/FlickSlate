package com.zsoltbertalan.flickslate.rules

import io.gitlab.arturbosch.detekt.test.lint
import io.kotest.matchers.collections.shouldHaveSize
import org.junit.Test

class NoSpaceIndentationRuleTest {

	@Test
	fun `reports spaces used for indentation`() {
		val code = """
			class Test {
			    fun test() {
			    }
			}
		""".trimIndent()
		val findings = NoSpaceIndentationRule().lint(code)
		findings shouldHaveSize 2
	}

	@Test
	fun `does not report tabs used for indentation`() {
		val code = """
			class Test {
				fun test() {
				}
			}
		""".trimIndent()
		val findings = NoSpaceIndentationRule().lint(code)
		findings shouldHaveSize 0
	}

	@Test
	fun `does not report block comment continuation spaces`() {
		val code = """
			/**
			 * Block comment
			 * with single space
			 */
			class Test
		""".trimIndent()
		val findings = NoSpaceIndentationRule().lint(code)
		findings shouldHaveSize 0
	}
}
