package com.feature.recipedetails.impl.test.usecase

import com.feature.recipedetails.api.model.DetailRecipe
import com.feature.recipedetails.impl.data.repository.RecipeDetailsRepository
import com.feature.recipedetails.impl.usecase.GetDetailRecipeByIdUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetDetailRecipeByIdUseCaseTest {


    lateinit var useCase: GetDetailRecipeByIdUseCase

    @MockK
    lateinit var mockRepository: RecipeDetailsRepository

    @Before
    fun setupData() {
        MockKAnnotations.init(this)
        useCase = GetDetailRecipeByIdUseCase(mockRepository)
    }

    @Test
    fun `invoke with valid id should return details`() = runBlocking {

        val mockDetails: DetailRecipe = mockk()
        val id = 1234L

        coEvery { mockRepository.getDetailRecipeById(id) } returns mockDetails

        val result = useCase(id)

        assertEquals(mockDetails, result)
    }

    @Test(expected = Exception::class)
    fun `invoke with repository throwing exception should propagate the exception`(): Unit = runBlocking {

        val id = 1234L
        coEvery { mockRepository.getDetailRecipeById(id) } throws Exception("Simulated exception")
        useCase(id)
    }
}