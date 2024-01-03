package com.feature.recipesearch.impl.test.usecase

import com.feature.recipesearch.api.model.Recipes
import com.feature.recipesearch.impl.data.repository.RecipesRepository
import com.feature.recipesearch.impl.usecase.GetRecipesByNameUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import org.junit.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetRecipesByNameUseCaseTest {

    lateinit var useCase: GetRecipesByNameUseCase

    @MockK
    lateinit var mockRepository: RecipesRepository

    @Before
    fun setupData() {
        MockKAnnotations.init(this)
        useCase = GetRecipesByNameUseCase(mockRepository)
    }

    @Test
    fun `invoke with valid recipe name should return recipes`() = runBlocking {

        val mockRecipes: Recipes = mockk()
        val name = "salad"

        coEvery { mockRepository.getRecipesByName(name) } returns mockRecipes
        val result = useCase(name)

        assertEquals(mockRecipes, result)
    }

    @Test(expected = Exception::class)
    fun `invoke with repository throwing exception should propagate the exception`(): Unit = runBlocking {

        val name = "pasta"

        coEvery { mockRepository.getRecipesByName(name) } throws Exception("Simulated exception")
        useCase(name)
    }

    @Test
    fun `invoke with non-existent recipe name should return empty recipes`() = runBlocking {

        val nonExistentRecipes = Recipes(emptyList())
        val name = "NonExistentRecipe"

        coEvery { mockRepository.getRecipesByName(name) } returns nonExistentRecipes
        val result = useCase(name)

        assertEquals(nonExistentRecipes, result)
    }

}