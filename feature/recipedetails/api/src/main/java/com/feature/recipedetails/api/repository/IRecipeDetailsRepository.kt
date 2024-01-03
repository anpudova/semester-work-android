package com.feature.recipesearch.api.repository

import com.feature.recipedetails.api.model.DetailRecipe
import com.feature.recipedetails.api.model.Ingredients

interface IRecipeDetailsRepository {

    suspend fun getIngredientsById(id: Long): Ingredients
    suspend fun getDetailRecipeById(id: Long): DetailRecipe
}