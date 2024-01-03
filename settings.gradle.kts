pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "RecipeAppProject"
include(":app")

include(":core")
include(":core:designsystem")
include(":core:utils")
include(":core:network")
include(":core:db")

include(":feature:profile")
include(":feature:favorites")
include(":feature:recipesearch")
include(":feature:recipedetails")

include(":feature:favorites:api")
include(":feature:favorites:impl")
include(":feature:profile:api")
include(":feature:profile:impl")
include(":feature:recipedetails:api")
include(":feature:recipedetails:impl")
include(":feature:recipesearch:api")
include(":feature:recipesearch:impl")
