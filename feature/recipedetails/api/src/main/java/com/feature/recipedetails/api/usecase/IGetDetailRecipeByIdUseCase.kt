package com.feature.recipedetails.api.usecase

import com.feature.recipedetails.api.model.DetailRecipe

interface IGetDetailRecipeByIdUseCase {

    suspend operator fun invoke(id: Long): DetailRecipe
}
