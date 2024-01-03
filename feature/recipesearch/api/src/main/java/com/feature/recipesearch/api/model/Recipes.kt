package com.feature.recipesearch.api.model

data class Recipes (
    val recipes: List<Recipe>? = null
) {

    data class Recipe (
        var id: Long,
        var title: String,
        var image: String,
        var imageType: String
    )
}