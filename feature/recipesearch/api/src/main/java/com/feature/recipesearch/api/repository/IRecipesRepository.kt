package com.feature.recipesearch.api.repository

import com.feature.recipesearch.api.model.Recipes

interface IRecipesRepository {

    suspend fun getRecipesByName(recipe: String): Recipes
}