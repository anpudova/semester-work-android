package com.feature.recipesearch.impl.usecase

import com.feature.recipesearch.api.model.Recipes
import com.feature.recipesearch.api.repository.IRecipesRepository
import com.feature.recipesearch.api.usecase.IGetRecipesByNameUseCase

class GetRecipesByNameUseCase (
    private val recipeRepository: IRecipesRepository
) {

    suspend operator fun invoke(recipe: String): Recipes {
        return recipeRepository.getRecipesByName(recipe)
    }
}