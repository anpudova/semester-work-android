package com.feature.recipesearch.impl.module

import com.feature.recipesearch.api.repository.IRecipesRepository
import com.feature.recipesearch.impl.usecase.GetRecipesByNameUseCase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class SearchDomainModule {

    @Provides
    fun provideGetRecipesByNameUseCase(repository: IRecipesRepository): GetRecipesByNameUseCase =
        GetRecipesByNameUseCase(repository)

}