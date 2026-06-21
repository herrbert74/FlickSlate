import dev.detekt.gradle.Detekt

plugins {
	alias(libs.plugins.detekt)
}

/**
 * Detekt configuration for the project.
 * It's enough to apply this script in the root project
 **/
tasks.register<Detekt>("detektAll") {
	description = ""
	parallel = true
	setSource(files(rootDir))
	pluginClasspath.from(configurations.detektPlugins)
	val rulesProject = rootProject.project(":rules")
	pluginClasspath.from(rulesProject.tasks.named("jar").map { it.outputs.files })
	dependsOn(rulesProject.tasks.named("jar"))
	reports {
		checkstyle {
			required = false
		}
		sarif {
			required = false // true by default, despite what docs say
		}
	}
	config = files(
		"$rootDir/config/detekt/default-detekt-config.yml",
		"$rootDir/config/detekt/compose-detekt-config.yml",
		"$rootDir/config/detekt/formatting-detekt-config.yml"
	)
	exclude("**/resources/**")
	exclude("**/build/**")
	exclude("**/bin/**")
	exclude("**/rules/**")
}

dependencies {
	detektPlugins(libs.detekt.compose)
	detektPlugins(libs.detekt.formatting)
}
