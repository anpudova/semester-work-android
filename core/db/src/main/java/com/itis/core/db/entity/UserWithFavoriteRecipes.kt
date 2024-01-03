package com.itis.core.db.entity

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithFavoriteRecipes(
    @Embedded val user: UserEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id_user"
    )
    val recipes: List<FavoriteRecipeEntity>
)
