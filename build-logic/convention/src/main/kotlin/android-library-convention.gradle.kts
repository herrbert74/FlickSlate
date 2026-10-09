import com.android.build.api.dsl.LibraryExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.abi.ExperimentalAbiValidation

plugins {
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.dependencyAnalysis)
}

extensions.configure<LibraryExtension>("android") {
	compileSdk = libs.versions.compileSdkVersion.get().toInt()
	defaultConfig {
		minSdk = libs.versions.minSdkVersion.get().toInt()
		val consumerRules = project.file("consumer-rules.pro")
		if (consumerRules.exists()) {
			consumerProguardFiles(consumerRules)
		}
	}
	// Robolectric 4.17 reflects into JDK internals such as SharedSecrets. On Java 17+ that access
	// is blocked unless these packages are opened, and unit tests fail with IllegalAccessException.
	// https://robolectric.org/getting-started/#running-with-java-17-and-higher
	// https://github.com/robolectric/robolectric.github.io/pull/625
	testOptions {
		unitTests.all {
			it.jvmArgs(
				"--add-opens=java.base/java.lang=ALL-UNNAMED",
				"--add-opens=java.base/java.util=ALL-UNNAMED",
				"--add-opens=java.base/java.io=ALL-UNNAMED",
				"--add-opens=java.base/java.net=ALL-UNNAMED",
				"--add-opens=java.base/java.security=ALL-UNNAMED",
				"--add-opens=java.base/java.text=ALL-UNNAMED",
				"--add-opens=java.base/jdk.internal.access=ALL-UNNAMED",
				"--add-opens=java.desktop/java.awt.font=ALL-UNNAMED",
				"--add-opens=jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED",
			)
		}
	}
}

extensions.configure<KotlinAndroidProjectExtension>("kotlin") {
	jvmToolchain(libs.versions.jdk.get().toInt())

	@OptIn(ExperimentalAbiValidation::class)
	abiValidation()
}
