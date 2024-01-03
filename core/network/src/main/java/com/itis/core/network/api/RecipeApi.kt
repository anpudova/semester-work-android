package com.itis.core.network.api

import com.itis.core.network.response.DetailRecipeResponse
import com.itis.core.network.response.IngredientResponse
import com.itis.core.network.response.RecipeResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApi {

    @GET("recipes/complexSearch?number=100")
    suspend fun getRecipeByName(
        @Query("query") recipe: String
    ): RecipeResponse

    @GET("/recipes/{id}/ingredientWidget.json")
    suspend fun getIngredientsById(
        @Path("id") id: Long
    ): IngredientResponse

    @GET("/recipes/{id}/analyzedInstructions?stepBreakdown=true")
    suspend fun getDetailRecipeById(
        @Path("id") id: Long
    ): DetailRecipeResponse

}