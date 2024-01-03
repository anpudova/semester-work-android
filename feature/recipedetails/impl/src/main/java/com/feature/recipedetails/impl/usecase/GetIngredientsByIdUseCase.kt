package com.feature.recipedetails.impl.usecase

import com.feature.recipedetails.api.model.Ingredients
import com.feature.recipedetails.api.usecase.IGetIngredientsByIdUseCase
import com.feature.recipesearch.api.repository.IRecipeDetailsRepository

class GetIngredientsByIdUseCase (
    private val recipeRepository: IRecipeDetailsRepository
) {

    suspend operator fun invoke(id: Long): Ingredients {
        return recipeRepository.getIngredientsById(id = id)
    }
}