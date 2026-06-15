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
    // Compose
    ":embedded:embedded-koin-compose",
    ":embedded:embedded-koin-compose-viewmodel",
    ":embedded:embedded-koin-compose-viewmodel-navigation",
    ":embedded:embedded-koin-androidx-compose",
    ":embedded:embedded-koin-androidx-compose-navigation",
    // Navigation
    ":embedded:embedded-koin-androidx-navigation",
    // Tests
    ":core:koin-test",
    ":core:koin-test-junit4",
)