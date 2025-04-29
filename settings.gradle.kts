pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral() // You can also add other repositories if required
        gradlePluginPortal() // For Gradle plugins
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") } // Fixed syntax for Kotlin DSL
    }
}

rootProject.name = "kidsplay" // Update with your project name
include(":app") // Make sure your app module is included