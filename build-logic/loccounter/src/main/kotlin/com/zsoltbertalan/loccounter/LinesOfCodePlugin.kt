package com.zsoltbertalan.loccounter

import kotlinx.serialization.json.Json
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Exec
import java.io.ByteArrayOutputStream
import java.io.File
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class LinesOfCodePlugin : Plugin<Project> {

	@OptIn(ExperimentalTime::class)
	override fun apply(project: Project) {
		if (project != project.rootProject) return

		val apiKey = project.findProperty("newRelicApiKey")?.toString()

		val subprojectInfos = getSubProjectInfos(project)

		project.tasks.register("uploadLocToNewRelic", Exec::class.java) {
			group = "reporting"
			description = "Uploads Kotlin and Java lines of code metrics to New Relic"

			doFirst {
				if (apiKey.isNullOrBlank()) {
					throw IllegalStateException(
						"New Relic credentials not found. " +
							"Please add 'newRelicApiKey' to your local.properties file."
					)
				}

				val locResults = calculateLinesOfCode(subprojectInfos)

				val timestamp = Clock.System.now().toEpochMilliseconds()

				val metrics = mutableListOf<Metric>()

				calculateMetrics(locResults, metrics, timestamp)

				val payload = listOf(
					NewRelicPayload(
						common = CommonBlock(
							attributes = CommonAttributes(
								serviceName = "FlickSlate-Android",
								host = "CI"
							)
						),
						metrics = metrics
					)
				)

				val jsonPayload = Json.encodeToString(payload)

				println("Uploading metrics to New Relic...")
				println(jsonPayload)

				val stdout = ByteArrayOutputStream()

				standardOutput = stdout
				commandLine("curl", "-vvv", "-L", "-X", "POST", "https://metric-api.eu.newrelic.com/metric/v1")
				args("-H", "Api-Key: $apiKey")
				args("-H", "Content-Type: application/json")
				args("-d", jsonPayload)

			}

			doLast {
				val stdout = ((this as Exec).standardOutput as? ByteArrayOutputStream)
				println("New Relic API response: ${stdout.toString().trim()}")
			}
		}

	}

	private fun calculateMetrics(
		locResults: LocResults,
		metrics: MutableList<Metric>,
		timestamp: Long
	) {
		locResults.modules.forEach { (name, lines) ->
			val sanitizedName = name.replace(":", ".").replace("-", "_")
			lines.forEach { codeLine ->
				metrics.add(
					Metric(
						"lines.of.code",
						value = codeLine.lines,
						timestamp = timestamp,
						attributes = MetricAttributes(module = sanitizedName.plus(".${codeLine.extension}"))
					)
				)
			}
		}
	}

	/**
	 * Calculates lines of code for Kotlin and Java in all subprojects.
	 */
	private fun calculateLinesOfCode(subprojectInfos: List<Pair<String, File>>): LocResults {
		fun countLinesInProject(projectDir: File, extensions: Set<String>): Map<String, Int> {
			val srcDirs = listOf(
				"src/main/java",
				"src/main/kotlin",
				"src/androidTest/java",
				"src/androidTest/kotlin",
				"src/test/java",
				"src/test/kotlin",
				"src/debug/kotlin",
				"src/screenshotTest/kotlin",
				"src/testFixtures/kotlin"
			)
			val counts = mutableMapOf<String, Int>().withDefault { 0 }
			srcDirs.forEach { dir ->
				val folder = File(projectDir, dir)
				if (!folder.exists()) return@forEach
				folder.walkTopDown()
					.filter { it.isFile && it.extension in extensions }
					.forEach { file ->
						val ext = file.extension
						counts[ext] = counts.getValue(ext) + file.readLines().size
					}
			}
			return counts
		}

		// Calculate for all modules
		val results = mutableMapOf<String, List<CodeLines>>()

		subprojectInfos.forEach { (name, dir) ->
			val lineCounts = countLinesInProject(dir, Extensions.entries.map { it.extension }.toSet())
			results[name] = lineCounts.map { (ext, lines) -> CodeLines(ext, lines) }
		}

		// Calculate for totals
		results["total"] = Extensions.entries.map { extEnum ->
			val ext = extEnum.extension
			val totalLines = results.values.sumOf { codeLinesList ->
				codeLinesList.filter { it.extension == ext }.sumOf { it.lines }
			}
			CodeLines(ext, totalLines)
		}

		// Printout
		results.forEach { (module, codeLineList) ->
			println("Module: $module")
			codeLineList.forEach { codeLines ->
				println("  ${codeLines.extension}: ${codeLines.lines} lines")
			}
		}

		return LocResults(results)
	}

	private fun getSubProjectInfos(project: Project): List<Pair<String, File>> {
		val subprojectInfos = project.subprojects
			.filter { sub -> sub.subprojects.isEmpty() }
			.map { sub ->
				val moduleName = if (sub.path.startsWith(":")) sub.path.substring(1) else sub.path
				moduleName to sub.projectDir
			}
		return subprojectInfos
	}
}
