package com.feature.recipedetails.api.model

import java.util.UUID

data class Ingredients (
    val ingredients: List<Ingredient>? = null
) {
    data class Ingredient (
        var id: UUID,
        var unit: String,
        var value: Float,
        var name: String
    )
}