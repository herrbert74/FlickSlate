plugins {
	alias(libs.plugins.metro)
}

metro {
	interop {
		includeDagger()
	}
}
