package com.zsoltbertalan.flickslate.rules

import dev.detekt.api.RuleSet
import dev.detekt.api.RuleSetId
import dev.detekt.api.RuleSetProvider

class FlickSlateRuleSetProvider : RuleSetProvider {
	override val ruleSetId: RuleSetId = RuleSetId("flickslate")

	override fun instance(): RuleSet = RuleSet(
		ruleSetId,
		listOf(
			{ config -> NoSpaceIndentationRule(config) },
			{ config -> MaxLineLengthTabs(config) },
		),
	)
}
