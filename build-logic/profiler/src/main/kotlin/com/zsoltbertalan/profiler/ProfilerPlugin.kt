package com.zsoltbertalan.profiler

import kotlinx.serialization.json.Json
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Exec
import org.gradle.kotlin.dsl.newInstance
import org.gradle.process.ExecOperations
import java.io.ByteArrayOutputStream
import java.io.File
import javax.inject.Inject
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

const val HEADER_LINE_COUNT = 4

/**
 * Runs the gradle-profiler and uploads results to New Relic.
 *
 * This has two command line tasks, which needs doLast, but from Gradle 9.0 we cannot use exec for it.
 * To understand why we need to inject execOp to use it in doLast, read:
 * https://discuss.gradle.org/t/replacing-deprecated-project-exec-in-dofirst-dolast/51381
 * https://stackoverflow.com/questions/79720795/replacing-deprecated-projectexec-in-dofirst-dolast
 *
 */
class ProfilerPlugin : Plugin<Project> {

	interface InjectedExecOps {
		@get:Inject
		val execOps: ExecOperations
	}

	@OptIn(ExperimentalTime::class)
	override fun apply(project: Project) {
		if (project != project.rootProject) return

		val apiKey = project.findProperty("newRelicApiKey")?.toString()

		val profilerExecutable = "/opt/homebrew/bin/gradle-profiler"

		val outputDir = project.layout.buildDirectory.file("profile-out").get().asFile
		val outputFile = outputDir.resolve("benchmark.csv")
		val rootDir = project.rootDir
		val scenarioFile = rootDir.resolve("scenarios.txt")

		project.tasks.register("profileAndUpload", Exec::class.java) {
			group = "profiling"
			description = "Runs gradle-profiler and uploads the results to New Relic"

			val injected = project.objects.newInstance<InjectedExecOps>()

			doFirst {
				commandLine(
					profilerExecutable,
					"--benchmark",
					"--project-dir", rootDir.absolutePath,
					"--scenario-file", scenarioFile.absolutePath,
					"--output-dir", outputDir.absolutePath,
					"--no-daemon"
				)
			}
			doLast {
				if (apiKey.isNullOrBlank()) {
					throw IllegalStateException(
						"New Relic API key not found. " +
							"Please add 'newRelicApiKey' to your local.properties file or as a project property."
					)
				}

				if (!outputFile.exists()) {
					println("Profiler output file not found: ${outputFile.absolutePath}")
					return@doLast
				}

				val metrics = parseProfilerOutput(outputFile)

				if (metrics.isEmpty()) {
					println("No metrics parsed from profiler output.")
					return@doLast
				}

				val timestamp = Clock.System.now().toEpochMilliseconds()
				val newRelicMetrics = metrics.map { (scenario, value) ->
					ProfilerMetric(
						name = "profiler.execution.time",
						value = value,
						timestamp = timestamp,
						attributes = mapOf("scenario" to scenario)
					)
				}

				val payload = listOf(
					ProfilerNewRelicPayload(
						common = ProfilerCommonBlock(
							attributes = ProfilerCommonAttributes(
								serviceName = "FlickSlate-Android",
								host = "CI"
							)
						),
						metrics = newRelicMetrics
					)
				)

				val jsonPayload = Json.encodeToString(payload)

				println("Uploading profiler metrics to New Relic...")
				println(jsonPayload)

				val stdout = ByteArrayOutputStream()

				injected.execOps.exec {
					standardOutput = stdout
					commandLine("curl", "-vvv", "-L", "-X", "POST", "https://metric-api.eu.newrelic.com/metric/v1")
					args("-H", "Api-Key: $apiKey")
					args("-H", "Content-Type: application/json")
					args("-d", jsonPayload)

				}
				println("New Relic API response: ${stdout.toString().trim()}")

			}
		}
	}

	private fun parseProfilerOutput(csvFile: File): Map<String, Double> {
		val metrics = addUpMetricsForEachScenario(csvFile)
		return getAverageResultForEachScenario(metrics)
	}

	private fun addUpMetricsForEachScenario(csvFile: File): MutableMap<String, MutableList<Double>> {
		val metrics = mutableMapOf<String, MutableList<Double>>()
		val header = csvFile.readLines().first().split(',')
		csvFile.readLines().drop(HEADER_LINE_COUNT).forEach { line ->
			val columns = line.split(',')
			if (columns[0].startsWith("measured")) {
				columns.forEachIndexed { index, metric ->
					if (index > 0) {
						val scenario = header[index].trim()
						val value = metric.trim().toDoubleOrNull() ?: 0f.toDouble()
						metrics[scenario]?.add(value) ?: run { metrics[scenario] = mutableListOf(value) }
					}
				}
			}
		}
		return metrics
	}

	private fun getAverageResultForEachScenario(
		metrics: MutableMap<String, MutableList<Double>>
	): Map<String, Double> {
		return metrics.mapValues { entry ->
			entry.value.average()
		}
	}
}
