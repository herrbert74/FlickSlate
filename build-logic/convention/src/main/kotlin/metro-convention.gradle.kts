
plugins {
	alias(libs.plugins.metro)
}

dependencies {
	add("implementation", libs.metro.runtime)
}
