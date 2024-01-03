package com.feature.recipesearch.impl.module

import com.feature.recipedetails.impl.data.repository.RecipeDetailsRepository
import com.feature.recipesearch.api.repository.IRecipeDetailsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DetailBindsModule {

    @Binds
    abstract fun bindRecipeDetailsRepository_to_IRecipeDetailsRepository(repository: RecipeDetailsRepository): IRecipeDetailsRepository
}