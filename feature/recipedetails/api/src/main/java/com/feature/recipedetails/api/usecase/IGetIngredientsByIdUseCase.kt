package com.feature.recipedetails.api.usecase

import com.feature.recipedetails.api.model.Ingredients

interface IGetIngredientsByIdUseCase {

    suspend operator fun invoke(id: Long): Ingredients
}