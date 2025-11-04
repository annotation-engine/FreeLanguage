plugins {
	alias(libs.plugins.kotlin.multiplatform)
}

group = "free.core"
version = "0.0.1"

kotlin {
	macosArm64 {
		binaries {
			executable {
				entryPoint = "free.core.main"
			}
		}
	}
	
	sourceSets {
		commonMain {
			dependencies {
				implementation(libs.bundles.free.core)
			}
		}
	}
}