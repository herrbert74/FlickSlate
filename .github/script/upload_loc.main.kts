#!/usr/bin/env kotlin

@file:DependsOn("com.google.code.gson:gson:2.13.2")

import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import java.io.File
import kotlin.system.exitProcess

// Data models
data class NewRelicPayload(
	val common: CommonBlock,
	val metrics: List<Metric>
)

data class CommonBlock(
	val attributes: CommonAttributes
)

data class CommonAttributes(
	@SerializedName("service.name") val serviceName: String,
	val host: String
)

data class Metric(
	val name: String,
	val type: String = "gauge",
	val value: Int,
	val timestamp: Long,
	val attributes: MetricAttributes? = null
)

data class MetricAttributes(
	val module: String
)

data class LocResults(
	val modules: Map<String, List<CodeLines>>
)

enum class Extensions(val extension: String) {
	Kotlin("kt"),
}

data class CodeLines(
	val extension: String,
	val lines: Int
)

// Main logic
fun main() {
	val apiKey = System.getenv("NEW_RELIC_API_KEY")
	if (apiKey.isNullOrBlank()) {
		System.err.println("Error: NEW_RELIC_API_KEY environment variable is not set")
		exitProcess(1)
	}

	val projectRoot = File(System.getProperty("user.dir"))
	println("Project root: ${projectRoot.absolutePath}")

	val subprojectInfos = getSubProjectInfos(projectRoot)
	val locResults = calculateLinesOfCode(subprojectInfos)
	val timestamp = System.currentTimeMillis()

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

	val gson = GsonBuilder().setPrettyPrinting().create()
	val jsonPayload = gson.toJson(payload)

	println("Uploading metrics to New Relic...")
	println(jsonPayload)

	uploadToNewRelic(apiKey, jsonPayload)
}

fun calculateMetrics(
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
fun calculateLinesOfCode(subprojectInfos: List<Pair<String, File>>): LocResults {
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

fun getSubProjectInfos(projectRoot: File): List<Pair<String, File>> {
	// Read settings.gradle.kts to discover all subprojects
	val settingsFile = File(projectRoot, "settings.gradle.kts")
	if (!settingsFile.exists()) {
		System.err.println("Error: settings.gradle.kts not found")
		exitProcess(1)
	}

	val subprojects = mutableListOf<Pair<String, File>>()

	// Manually discover subprojects by scanning the directory structure
	// Looking for build.gradle.kts files in subdirectories
	val topLevelDirs = listOf("app", "base", "feature", "shared", "build-logic")

	fun scanDirectory(dir: File, prefix: String = "") {
		if (!dir.isDirectory) return

		val buildFile = File(dir, "build.gradle.kts")
		if (buildFile.exists()) {
			// Check if it has subdirectories with build files
			val hasSubprojects = dir.listFiles()?.any {
				it.isDirectory && File(it, "build.gradle.kts").exists()
			} ?: false

			if (!hasSubprojects) {
				val moduleName = if (prefix.isEmpty()) dir.name else "$prefix/${dir.name}"
				subprojects.add(moduleName to dir)
			}
		}

		// Recursively scan subdirectories
		dir.listFiles()?.forEach { subDir ->
			if (subDir.isDirectory && !subDir.name.startsWith(".") && subDir.name != "build") {
				val newPrefix = if (prefix.isEmpty()) dir.name else "$prefix/${dir.name}"
				scanDirectory(subDir, newPrefix)
			}
		}
	}

	topLevelDirs.forEach { dirName ->
		val dir = File(projectRoot, dirName)
		if (dir.exists()) {
			scanDirectory(dir)
		}
	}

	println("Found ${subprojects.size} subprojects")
	subprojects.forEach { (name, _) ->
		println("  - $name")
	}

	return subprojects
}

fun uploadToNewRelic(apiKey: String, jsonPayload: String) {
	val process = ProcessBuilder(
		"curl", "-vvv", "-L", "-X", "POST",
		"https://metric-api.eu.newrelic.com/metric/v1",
		"-H", "Api-Key: $apiKey",
		"-H", "Content-Type: application/json",
		"-d", jsonPayload
	).redirectErrorStream(true)
		.start()

	val output = process.inputStream.bufferedReader().readText()
	val exitCode = process.waitFor()

	println("\nNew Relic API response:")
	println(output)

	if (exitCode != 0) {
		System.err.println("Error: curl command failed with exit code $exitCode")
		exitProcess(exitCode)
	}
}

// Run main
main()

