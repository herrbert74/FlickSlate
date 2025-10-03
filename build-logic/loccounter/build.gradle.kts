plugins {
	`kotlin-dsl`
}

group = "com.zsoltbertalan.flickslate.buildlogic.loccounter"

kotlin {
	jvmToolchain(21)
}

gradlePlugin {
	plugins {
		create("linesOfCode") {
			id = "loccounter"
			implementationClass = "com.zsoltbertalan.loccounter.LinesOfCodePlugin"
		}
	}
}
