import io.gitlab.arturbosch.detekt.Detekt

plugins {
	alias(libs.plugins.detekt)
}

/**
 * Detekt configuration for the project.
 * It's enough to apply this script in the root project
 **/
tasks.register<Detekt>("detektAll") {
	parallel = true
	setSource(files(rootDir))
	reports {
		xml {
			required = false
		}
		txt {
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
}

dependencies {
	detektPlugins(libs.detekt.compose)
	detektPlugins(libs.detekt.formatting)
}
