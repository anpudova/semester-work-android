package com.feature.recipedetails.impl.test.usecase

import com.feature.recipedetails.api.model.Ingredients
import com.feature.recipedetails.impl.data.repository.RecipeDetailsRepository
import com.feature.recipedetails.impl.usecase.GetIngredientsByIdUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import org.junit.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetIngredientsByIdUseCaseTest {

    lateinit var useCase: GetIngredientsByIdUseCase

    @MockK
    lateinit var mockRepository: RecipeDetailsRepository

    @Before
    fun setupData() {
        MockKAnnotations.init(this)
        useCase = GetIngredientsByIdUseCase(mockRepository)
    }

    @Test
    fun `invoke with valid id should return ingredients`() = runBlocking {

        val mockIngredients: Ingredients = mockk()
        val id = 1234L

        coEvery { mockRepository.getIngredientsById(id) } returns mockIngredients
        val result = useCase(id)
        assertEquals(mockIngredients, result)
    }

    @Test(expected = Exception::class)
    fun `invoke with repository throwing exception should propagate the exception`(): Unit = runBlocking {

        val id = 1234L

        coEvery { mockRepository.getIngredientsById(id) } throws Exception("Simulated exception")

        useCase(id)
    }
}