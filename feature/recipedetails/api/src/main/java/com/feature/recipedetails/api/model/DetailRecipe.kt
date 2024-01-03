package com.feature.recipedetails.api.model

import java.util.UUID

data class DetailRecipe (
    val name: String? = null,
    val steps: List<Step>? = null
) {

    data class Step(
        var id: UUID,
        var number: Int,
        var step: String
    )
}