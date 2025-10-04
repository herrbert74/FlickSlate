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
		println("key: $apiKey")
		val locResults = calculateLinesOfCode(project)

		project.tasks.register("uploadLocToNewRelic", Exec::class.java) {
			group = "reporting"
			description = "Uploads Kotlin and Java lines of code metrics to New Relic"

			doFirst {
				if (apiKey.isNullOrBlank()) {
					throw IllegalStateException(
						"New Relic credentials not found. " +
							"Please add 'newRelicApiKey' and 'newRelicAccountId' to your local.properties file."
					)
				}

				val timestamp = Clock.System.now().toEpochMilliseconds()

				val metrics = mutableListOf<Metric>()

				metrics.add(Metric(name = "loc.total.kotlin", value = locResults.totalKotlin, timestamp = timestamp))
				metrics.add(Metric(name = "loc.total.java", value = locResults.totalJava, timestamp = timestamp))

				locResults.modules.forEach { (name, lines) ->
					val sanitizedName = name.replace(":", ".").replace("-", "_")
					val attributes = MetricAttributes(module = sanitizedName)
					metrics.add(
						Metric(
							"loc.module.kotlin",
							value = lines.first,
							timestamp = timestamp,
							attributes = attributes
						)
					)
					metrics.add(
						Metric(
							"loc.module.java",
							value = lines.second,
							timestamp = timestamp,
							attributes = attributes
						)
					)
				}

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
				commandLine("curl", "-L", "-X", "POST", "https://metric-api.eu.newrelic.com/metric/v1")
				args("-H", "Api-Key: $apiKey")
				args("-H", "Content-Type: application/json")
				args("-d", jsonPayload)

				println("New Relic API response: ${stdout.toString().trim()}")
			}
		}

	}

	/**
	 * Calculates lines of code for Kotlin and Java in all subprojects.
	 */
	private fun calculateLinesOfCode(project: Project): LocResults {
		val subprojectInfos = project.subprojects
			.filter { sub -> sub.subprojects.isEmpty() }
			.map { sub ->
				val name = StringBuilder()
				name.append(sub.name)
				var parent = sub.parent
				while (parent != null && parent != project.rootProject) {
					name.insert(0, "${parent.name}:")
					parent = parent.parent
				}
				name.toString() to sub.projectDir
			}

		fun countLinesInProject(projectDir: File, ext: String): Int {
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
			return srcDirs.sumOf { dir ->
				val folder = File(projectDir, dir)
				if (!folder.exists()) return@sumOf 0
				folder.walkTopDown()
					.filter { it.isFile && it.extension == ext }
					.sumOf { file -> file.readLines().size }
			}
		}

		val results = mutableMapOf<String, Pair<Int, Int>>()
		var totalKotlin = 0
		var totalJava = 0

		subprojectInfos.forEach { (name, dir) ->
			val kotlinLines = countLinesInProject(dir, "kt")
			val javaLines = countLinesInProject(dir, "java")
			results[name] = kotlinLines to javaLines
			totalKotlin += kotlinLines
			totalJava += javaLines
		}

		println("Lines of code per module:")
		results.forEach { (name, lines) ->
			println("  $name: Kotlin=${lines.first}, Java=${lines.second}")
		}
		println("Total lines of code: Kotlin=$totalKotlin, Java=$totalJava")

		return LocResults(totalKotlin, totalJava, results)
	}
}
