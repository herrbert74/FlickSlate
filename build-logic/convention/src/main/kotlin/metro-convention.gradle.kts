plugins {
	alias(libs.plugins.metro)
}

fun excludeFrom(excluded: List<String>, action: () -> Unit) {
	if (!excluded.contains(project.name)) {
		action()
	}
}

dependencies {
	excludeFrom(listOf("domain", "data", "kotlin", "android")) {
		"implementation"(libs.metrox.viewmodelCompose)
	}

	if (project.name == "app") {
		"implementation"(libs.metrox.viewmodel)
	}
}
