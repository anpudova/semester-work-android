package com.feature.recipedetails.impl.data.mapper

import com.feature.recipedetails.api.model.DetailRecipe
import com.itis.core.network.response.DetailRecipeResponse
import java.util.UUID
import javax.inject.Inject

class DetailRecipeResponseMapper @Inject constructor() {

    fun map(resp: DetailRecipeResponse?): DetailRecipe {
        val listStep = mutableListOf<DetailRecipe.Step>()
        return resp?.let { response ->
            with(response) {
                get(0).steps?.let { steps ->
                    steps.forEach { item ->
                        listStep.add(
                            DetailRecipe.Step(
                                UUID.randomUUID(),
                                item.number.orEmpty(),
                                item.step.toString()
                            )
                        )
                    }
                }
                DetailRecipe(
                    get(0).name.toString(),
                    listStep)
            }
        } ?: DetailRecipe(
            "",
            emptyList()
        )
    }

    private fun Int?.orEmpty() =
        this ?: 0
}