package com.feature.recipesearch.impl.module

import com.feature.recipesearch.api.repository.IRecipesRepository
import com.feature.recipesearch.impl.data.repository.RecipesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SearchBindsModule {

    @Binds
    abstract fun bindRecipesRepository_to_IRecipesRepository(repository: RecipesRepository): IRecipesRepository
}