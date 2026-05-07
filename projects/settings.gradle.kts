enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupByRegex(".*google.*")
                includeGroupByRegex(".*android.*")
            }
        }
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupByRegex(".*google.*")
                includeGroupByRegex(".*android.*")
            }
        }
        // For Ktor EAP
//        maven("https://maven.pkg.jetbrains.space/public/p/ktor/eap") {
//            mavenContent {
//                includeGroupAndSubgroups("io.ktor")
//            }
//        }
        mavenCentral()
    }
}

include(
    // Core
    ":embedded:embedded-koin-core-annotations",
    ":embedded:embedded-koin-core",
    ":embedded:embedded-koin-core-viewmodel",
    ":embedded:embedded-koin-android",
    ":core:koin-test",
    ":core:koin-test-junit4",
)