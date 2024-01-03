package com.feature.recipesearch.impl.module

import com.feature.recipedetails.impl.usecase.GetDetailRecipeByIdUseCase
import com.feature.recipedetails.impl.usecase.GetIngredientsByIdUseCase
import com.feature.recipesearch.api.repository.IRecipeDetailsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DetailDomainModule {

    @Provides
    fun provideGetDetailRecipeByIdUseCase(repository: IRecipeDetailsRepository): GetDetailRecipeByIdUseCase =
        GetDetailRecipeByIdUseCase(repository)

    @Provides
    fun provideGetIngredientsByIdUseCase(repository: IRecipeDetailsRepository): GetIngredientsByIdUseCase =
        GetIngredientsByIdUseCase(repository)
}