plugins {
	alias(libs.plugins.ksp)
}

dependencies {
	"api"(project(":base:kotlin"))
	"api"(project(":shared:data"))
	"api"(project(":shared:domain"))

	"api"(libs.retrofit)

	excludeFrom("account") {
		"implementation"(libs.androidx.roomCommon)
	}
	"implementation"(libs.androidx.roomRuntime)
	excludeFrom("account") {
		"implementation"(libs.androidx.sqlite)
	}
	"implementation"(libs.kotlinResult.result)
	excludeFrom("account") {
		"implementation"(libs.kotlinRetry)
	}
	excludeFrom("account") {
		"implementation"(libs.kotlinx.collectionsImmutableJvm)
		"implementation"(libs.kotlinx.coroutinesCore)
	}
	"implementation"(libs.kotlinx.serializationCore)
	"implementation"(libs.okhttp3)
	"implementation"(libs.timber)

	"ksp"(libs.androidx.roomCompiler)

	"testImplementation"(libs.jUnit)
	"testImplementation"(libs.mockk.library)
	"testImplementation"(libs.mockk.core)
	"testImplementation"(libs.mockk.dsl) // transitive
	"testImplementation"(libs.kotlinx.coroutinesTest)
	"testImplementation"(libs.kotlinx.serializationJson)
}

fun excludeFrom(excluded: String, dependency: () -> Unit) {
	if (!(project.parent?.name.equals(excluded))) {
		dependency()
	}
}
