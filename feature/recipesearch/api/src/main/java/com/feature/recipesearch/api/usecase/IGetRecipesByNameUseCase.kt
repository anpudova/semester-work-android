package com.feature.recipesearch.api.usecase

import com.feature.recipesearch.api.model.Recipes

interface IGetRecipesByNameUseCase {

    suspend operator fun invoke(recipe: String): Recipes
}