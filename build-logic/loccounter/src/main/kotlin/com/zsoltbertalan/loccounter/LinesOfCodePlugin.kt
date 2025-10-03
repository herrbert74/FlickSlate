package com.zsoltbertalan.loccounter

import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

class LinesOfCodePlugin : Plugin<Project> {

	override fun apply(project: Project) {
		if (project != project.rootProject) return

		project.tasks.register("countLinesOfCode") {
			group = "verification"
			description = "Counts Kotlin and Java lines of code in all modules"

			// Collect serializable data only
			val subprojectInfos = project.subprojects
				.filter { sub -> sub.subprojects.isEmpty() }
				.map { sub ->
					val name = StringBuilder()
					name.append(sub.name)
					val parent = sub.parent
					if (parent != null && parent != sub.rootProject) {
						name.insert(0, "${parent.name}.")
						val parent2 = parent.parent
						if (parent2 != null && parent2 != sub.rootProject) {
							name.insert(0, "${parent2.name}.")
						}
					}
					name.toString() to sub.projectDir
				}

			doLast {
				fun countLines(projectDir: File, ext: String): Int {
					println("subprojects: $subprojectInfos")
					val srcDirs = listOf(
						"src/main/java",
						"src/androidTest/java",
						"src/test/java",
						"src/debug/kotlin",
						"src/screenshotTest/kotlin",
						"src/testFixtures/kotlin"
					)
					return srcDirs.sumOf { dir ->
						val folder = File(projectDir, dir)
						if (!folder.exists()) return@sumOf 0
						folder.walkTopDown()
							.filter { it.isFile && it.extension == ext }
							.sumOf { it.readLines().size }
					}
				}

				val results = mutableMapOf<String, Pair<Int, Int>>()
				var totalKotlin = 0
				var totalJava = 0

				subprojectInfos.forEach { (name, dir) ->
					val kotlinLines = countLines(dir, "kt")
					val javaLines = countLines(dir, "java")
					results[name] = kotlinLines to javaLines
					totalKotlin += kotlinLines
					totalJava += javaLines
				}

				println("Lines of code per module:")
				results.forEach { (name, lines) ->
					println("  $name: Kotlin=${lines.first}, Java=${lines.second}")
				}
				println("Total lines of code: Kotlin=$totalKotlin, Java=$totalJava")
			}
		}

	}
}
