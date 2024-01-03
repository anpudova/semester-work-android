package com.feature.recipesearch.impl.data.repository

import com.feature.recipesearch.api.model.Recipes
import kotlinx.coroutines.Dispatchers
import com.feature.recipesearch.api.repository.IRecipesRepository
import com.feature.recipesearch.impl.data.mapper.RecipeResponseMapper
import com.itis.core.network.api.RecipeApi
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecipesRepository @Inject constructor (
    private val remoteSource: RecipeApi,
    private val recipeResponseMapper: RecipeResponseMapper
): IRecipesRepository {

    override suspend fun getRecipesByName(recipe: String): Recipes {
        return withContext(Dispatchers.IO) {
            (recipeResponseMapper::map)(remoteSource.getRecipeByName(recipe = recipe))
        }
    }
}