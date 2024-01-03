package com.itis.feature.favorites.impl.mapper

import com.itis.core.db.entity.FavoriteRecipeEntity
import com.itis.feature.favorites.api.model.FavoriteRecipe

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