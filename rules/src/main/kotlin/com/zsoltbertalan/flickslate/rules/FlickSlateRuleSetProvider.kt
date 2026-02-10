package com.zsoltbertalan.flickslate.rules

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider

class FlickSlateRuleSetProvider : RuleSetProvider {
	override val ruleSetId: String = "flickslate"

	override fun instance(config: Config): RuleSet {
		return RuleSet(
			ruleSetId,
			listOf(
				NoSpaceIndentationRule(config),
				MaxLineLengthTabs(config),
			)
		)
	}
}
