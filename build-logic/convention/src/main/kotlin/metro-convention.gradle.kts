listOf(
	"com.android.application",
	"com.android.library",
	"org.jetbrains.kotlin.jvm",
	"org.jetbrains.kotlin.android",
	"org.jetbrains.kotlin.multiplatform"
).forEach { pluginId ->
	pluginManager.withPlugin(pluginId) {
		if (!pluginManager.hasPlugin("dev.zacsweers.metro")) {
			apply(plugin = "dev.zacsweers.metro")
		}
	}
}

fun excludeFrom(excluded: List<String>, action: () -> Unit) {
	if (!excluded.contains(project.name)) {
		action()
	}
}

dependencies {
	"implementation"(libs.metro.runtime)

	excludeFrom(listOf("domain", "data", "kotlin", "android")) {
		"implementation"(libs.metrox.viewmodelCompose)
	}

	if (project.name == "app") {
		"implementation"(libs.metrox.viewmodel)
	}
}
