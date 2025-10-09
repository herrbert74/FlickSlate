package com.zsoltbertalan.profiler

import kotlinx.serialization.json.Json
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Exec
import java.io.ByteArrayOutputStream
import java.io.File
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class ProfilerPlugin : Plugin<Project> {

    @OptIn(ExperimentalTime::class)
    override fun apply(project: Project) {
        if (project != project.rootProject) return

        project.tasks.register("profileAndUpload", Exec::class.java) {
            group = "profiling"
            description = "Runs gradle-profiler and uploads the results to New Relic"

            // gradle-profiler must be on the system's PATH
            val profilerExecutable = "gradle-profiler"
            val outputDir = project.buildDir.resolve("profiler-out")
            val outputFile = outputDir.resolve("benchmark.csv")
            val scenarioFile = project.rootDir.resolve("scenarios.txt")

            // Configure the exec task to run gradle-profiler
            commandLine(
                profilerExecutable,
                "--benchmark",
                "--project-dir", project.rootDir.absolutePath,
                "--scenario-file", scenarioFile.absolutePath,
                "--output-dir", outputDir.absolutePath,
                "--output-format", "csv"
            )

            doLast {
                // After the profiler runs, parse the output and upload it.
                val apiKey = project.findProperty("newRelicApiKey")?.toString()
                if (apiKey.isNullOrBlank()) {
                    throw IllegalStateException(
                        "New Relic API key not found. " +
                            "Please add 'newRelicApiKey' to your local.properties file or as a project property."
                    )
                }

                if (!outputFile.exists()) {
                    project.logger.warn("Profiler output file not found: ${outputFile.absolutePath}")
                    return@doLast
                }

                val metrics = parseProfilerOutput(outputFile)
                if (metrics.isEmpty()) {
                    project.logger.warn("No metrics parsed from profiler output.")
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

                project.logger.lifecycle("Uploading profiler metrics to New Relic...")
                project.logger.info(jsonPayload)

                val stdout = ByteArrayOutputStream()
                project.exec {
                    it.commandLine("curl", "-L", "-X", "POST", "https://metric-api.eu.newrelic.com/metric/v1")
                    it.args("-H", "Api-Key: $apiKey")
                    it.args("-H", "Content-Type: application/json")
                    it.args("-d", jsonPayload)
                    it.standardOutput = stdout
                }

                project.logger.lifecycle("New Relic API response: ${stdout.toString().trim()}")
            }
        }
    }

    private fun parseProfilerOutput(csvFile: File): Map<String, Double> {
        val metrics = mutableMapOf<String, Double>()
        // The first line is the header
        csvFile.readLines().drop(1).forEach { line ->
            val columns = line.split(',')
            if (columns.size >= 4) {
                // Assuming format: scenario,version,value,metric
                // We're interested in 'execution_time'
                val metricName = columns[3]
                if (metricName.trim() == "execution_time") {
                    val scenario = columns[0].trim()
                    val value = columns[2].trim().toDoubleOrNull()
                    if (value != null) {
                        metrics[scenario] = value
                    }
                }
            }
        }
        return metrics
    }
}