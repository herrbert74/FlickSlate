#!/usr/bin/env kotlin

@file:DependsOn("com.google.code.gson:gson:2.13.2")

import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import java.io.File
import kotlin.system.exitProcess

val headerLineCount = 4

// Data models
data class ProfilerNewRelicPayload(
    val common: ProfilerCommonBlock,
    val metrics: List<ProfilerMetric>
)

data class ProfilerCommonBlock(
    val attributes: ProfilerCommonAttributes
)

data class ProfilerCommonAttributes(
    @SerializedName("service.name") val serviceName: String,
    val host: String
)

data class ProfilerMetric(
    val name: String,
    val type: String = "gauge",
    val value: Double,
    val timestamp: Long,
    val attributes: Map<String, String>? = null
)

data class ProfilerResult(
    val summary: String,
    val results: Map<String, String>
)

// Main logic
fun main() {
    val apiKey = System.getenv("NEW_RELIC_API_KEY")
    if (apiKey.isNullOrBlank()) {
        System.err.println("Error: NEW_RELIC_API_KEY environment variable is not set")
        exitProcess(1)
    }

    val profilerExecutable = "/opt/homebrew/bin/gradle-profiler"
    val projectRoot = File(System.getProperty("user.dir"))
    val outputDir = File(projectRoot, "build/profile-out")
    val outputFile = File(outputDir, "benchmark.csv")
    val scenarioFile = File(projectRoot, "scenarios.txt")

    // Create output directory
    outputDir.mkdirs()

    // Run gradle-profiler
    println("Running gradle-profiler...")
    val process = ProcessBuilder(
        profilerExecutable,
        "--benchmark",
        "--project-dir", projectRoot.absolutePath,
        "--scenario-file", scenarioFile.absolutePath,
        "--output-dir", outputDir.absolutePath,
        "--no-daemon"
    ).redirectErrorStream(true)
        .start()

    val exitCode = process.waitFor()
    if (exitCode != 0) {
        System.err.println("Error: gradle-profiler command failed with exit code $exitCode")
        exitProcess(exitCode)
    }

    if (!outputFile.exists()) {
        println("Profiler output file not found: ${outputFile.absolutePath}")
        exitProcess(1)
    }

    val metrics = parseProfilerOutput(outputFile)

    if (metrics.isEmpty()) {
        println("No metrics parsed from profiler output.")
        exitProcess(1)
    }

    val timestamp = System.currentTimeMillis()
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

    val gson = GsonBuilder().setPrettyPrinting().create()
    val jsonPayload = gson.toJson(payload)

    println("Uploading profiler metrics to New Relic...")
    println(jsonPayload)

    // Upload to New Relic
    val uploadProcess = ProcessBuilder(
        "curl", "-vvv", "-L", "-X", "POST",
        "https://metric-api.eu.newrelic.com/metric/v1",
        "-H", "Api-Key: $apiKey",
        "-H", "Content-Type: application/json",
        "-d", jsonPayload
    ).redirectErrorStream(true)
        .start()

    val uploadOutput = uploadProcess.inputStream.bufferedReader().readText()
    val uploadExitCode = uploadProcess.waitFor()

    println("\nNew Relic API response:")
    println(uploadOutput)

    if (uploadExitCode != 0) {
        System.err.println("Error: curl command failed with exit code $uploadExitCode")
        exitProcess(uploadExitCode)
    }
}

private fun parseProfilerOutput(csvFile: File): Map<String, Double> {
    val metrics = addUpMetricsForEachScenario(csvFile)
    return getAverageResultForEachScenario(metrics)
}

private fun addUpMetricsForEachScenario(csvFile: File): MutableMap<String, MutableList<Double>> {
    val metrics = mutableMapOf<String, MutableList<Double>>()
    val header = csvFile.readLines().first().split(',')
    csvFile.readLines().drop(headerLineCount).forEach { line ->
        val columns = line.split(',')
        if (columns[0].startsWith("measured")) {
            columns.forEachIndexed { index, metric ->
                if (index > 0) {
                    val scenario = header[index].trim()
                    val value = metric.trim().toDoubleOrNull() ?: 0.0
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

// Run main
main()
