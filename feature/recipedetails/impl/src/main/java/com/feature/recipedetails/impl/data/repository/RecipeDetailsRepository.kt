package com.feature.recipedetails.impl.data.repository

import com.feature.recipedetails.api.model.DetailRecipe
import com.feature.recipedetails.api.model.Ingredients
import com.feature.recipedetails.impl.data.mapper.DetailRecipeResponseMapper
import com.feature.recipedetails.impl.data.mapper.IngredientResponseMapper
import com.feature.recipesearch.api.repository.IRecipeDetailsRepository
import com.itis.core.network.api.RecipeApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecipeDetailsRepository @Inject constructor(
    private val remoteSource: RecipeApi,
    private val ingredientResponseMapper: IngredientResponseMapper,
    private val detailRecipeResponseMapper: DetailRecipeResponseMapper
): IRecipeDetailsRepository {

    override suspend fun getIngredientsById(id: Long): Ingredients {
        return withContext(Dispatchers.IO) {
            (ingredientResponseMapper::map)(remoteSource.getIngredientsById(id = id))
        }
    }

    override suspend fun getDetailRecipeById(id: Long): DetailRecipe {
        return withContext(Dispatchers.IO) {
            (detailRecipeResponseMapper::map)(remoteSource.getDetailRecipeById(id = id))
        }
    }
}