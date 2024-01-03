package com.feature.recipedetails.impl.usecase

import com.feature.recipedetails.api.model.DetailRecipe
import com.feature.recipedetails.api.usecase.IGetDetailRecipeByIdUseCase
import com.feature.recipesearch.api.repository.IRecipeDetailsRepository

class GetDetailRecipeByIdUseCase (
    private val recipeRepository: IRecipeDetailsRepository
) {

    suspend operator fun invoke(id: Long): DetailRecipe {
        return recipeRepository.getDetailRecipeById(id = id)
    }
}
