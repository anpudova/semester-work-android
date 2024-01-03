package com.itis.core.db.dao

import androidx.room.*
import com.itis.core.db.entity.FavoriteRecipeEntity

@Dao
interface FavoriteRecipeDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun createFavoriteRecipe(recipe: FavoriteRecipeEntity)

    @Delete
    suspend fun deleteFavoriteRecipe(recipe: FavoriteRecipeEntity)

    @Query("select * from favorite_recipes where id_user = :idUser")
    suspend fun getFavoriteRecipes(idUser: Long): List<FavoriteRecipeEntity>?

    @Query("select count(*) from favorite_recipes where id = :id and id_user = :idUser")
    suspend fun existInFavorites(id: Long, idUser: Long): Int?
}