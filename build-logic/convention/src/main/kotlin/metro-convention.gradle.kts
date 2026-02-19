listOf(
	"com.android.application",
	"com.android.library",
	"org.jetbrains.kotlin.jvm",
	"org.jetbrains.kotlin.android",
	"org.jetbrains.kotlin.multiplatform"
).forEach { pluginId ->
	pluginManager.withPlugin(pluginId) {
		if (project.path == ":base:kotlin") {
			return@withPlugin
		}
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
	if (project.path == ":base:kotlin") {
		"compileOnly"(libs.metro.runtime)
		return@dependencies
	}

	excludeFrom(listOf("domain")) {
		"implementation"(libs.metro.runtime)
	}

	excludeFrom(listOf("domain", "data", "kotlin", "android")) {
		"implementation"(libs.metrox.viewmodelCompose)
	}

	if (project.name == "app" || project.name == "ui") {
		"implementation"(libs.metrox.viewmodel)
	}
}
