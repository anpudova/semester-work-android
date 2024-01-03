package com.itis.feature.favorites.api.model

data class FavoriteRecipe (
    val id: Long,
    val name: String,
    val image: String,
    val idUser: Long
)
