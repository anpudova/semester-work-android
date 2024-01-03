package com.feature.recipedetails.impl.data.mapper

import com.feature.recipedetails.api.model.FavoriteRecipe
import com.itis.core.db.entity.FavoriteRecipeEntity

object FavoriteRecipeMapper {

    fun mapFavoriteRecipeEntity(recipe: FavoriteRecipe): FavoriteRecipeEntity {
        with(recipe) {
            return FavoriteRecipeEntity(
                id, name, image, idUser
            )
        }
    }

    fun mapFavoriteRecipeModel(recipe: FavoriteRecipeEntity): FavoriteRecipe {
        with(recipe) {
            return FavoriteRecipe(
                id, name, image, idUser
            )
        }
    }

}