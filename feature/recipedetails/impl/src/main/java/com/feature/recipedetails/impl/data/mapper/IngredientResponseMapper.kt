package com.feature.recipedetails.impl.data.mapper

import com.feature.recipedetails.api.model.Ingredients
import com.itis.core.network.response.IngredientResponse
import java.util.UUID
import javax.inject.Inject

class IngredientResponseMapper @Inject constructor() {

    fun map(resp: IngredientResponse?): Ingredients {
        val list = mutableListOf<Ingredients.Ingredient>()
        return resp?.let { response ->
            with(response) {
                result?.let { ingredients ->
                    ingredients.forEach { item ->
                        list.add(Ingredients.Ingredient(
                            UUID.randomUUID(),
                            item.amount?.metric?.unit.toString(),
                            item.amount?.metric?.value.orEmpty(),
                            item.name.toString()
                        )
                        )
                    }
                }
                return Ingredients(list)
            }
        } ?: Ingredients()
    }

    private fun Float?.orEmpty() =
        this ?: 0F
}
