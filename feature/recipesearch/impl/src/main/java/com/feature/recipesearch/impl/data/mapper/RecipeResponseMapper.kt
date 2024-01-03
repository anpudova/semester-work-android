package com.feature.recipesearch.impl.data.mapper

import com.feature.recipesearch.api.model.Recipes
import com.itis.core.network.response.RecipeResponse
import javax.inject.Inject

class RecipeResponseMapper @Inject constructor() {

    fun map(resp: RecipeResponse?): Recipes {
        val list = mutableListOf<Recipes.Recipe>()
        return resp?.let { response ->
            with(response) {
                result?.let { recipes ->
                    recipes.forEach { item ->
                        item.id?.let { id ->
                            Recipes.Recipe(
                                id,
                                item.title.toString(),
                                item.image.toString(),
                                item.imageType.toString()
                            )
                        }?.let { recipeEntity ->
                            list.add(
                                recipeEntity
                            )
                        }
                    }
                }
                return Recipes(list)
            }
        } ?: Recipes()
    }
}