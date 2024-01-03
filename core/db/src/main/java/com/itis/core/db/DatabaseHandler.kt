package com.itis.core.db

import android.content.Context
import androidx.room.Room
import com.itis.core.db.entity.FavoriteRecipeEntity
import com.itis.core.db.entity.UserEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseHandler {

    const val versiondb: Int = 1
    const val namedb: String = "dbRecipes"
    private var roomDatabase: InceptionDatabase? = null

    fun provideDatabase(appContext: Context) {
        if (roomDatabase == null) {
            roomDatabase = Room.databaseBuilder(
                appContext,
                InceptionDatabase::class.java,
                namedb
            ).build()
        }
    }

    suspend fun createUser(user: UserEntity) {
        withContext(Dispatchers.IO) {
            roomDatabase?.getUserDao()?.createUser(user)
        }
    }

    suspend fun getUser(username: String, password: String): UserEntity? {
        return withContext(Dispatchers.IO) {
            roomDatabase?.getUserDao()?.getUser(username, password)
        }
    }

    suspend fun getUsername(username: String): String? {
        return withContext(Dispatchers.IO) {
            roomDatabase?.getUserDao()?.getUsername(username)
        }
    }

    suspend fun getUserByUsername(username: String): UserEntity? {
        return withContext(Dispatchers.IO) {
            roomDatabase?.getUserDao()?.getUserByUsername(username)
        }
    }

    suspend fun deleteUser(user: UserEntity) {
        withContext(Dispatchers.IO) {
            roomDatabase?.getUserDao()?.deleteUser(user)
        }
    }

    suspend fun createFavoriteRecipe(recipe: FavoriteRecipeEntity) {
        withContext(Dispatchers.IO) {
            roomDatabase?.getFavoriteRecipeDao()?.createFavoriteRecipe(recipe)
        }
    }

    suspend fun deleteFavoriteRecipe(recipe: FavoriteRecipeEntity) {
        withContext(Dispatchers.IO) {
            roomDatabase?.getFavoriteRecipeDao()?.deleteFavoriteRecipe(recipe)
        }
    }

    suspend fun getFavoriteRecipes(idUser: Long): List<FavoriteRecipeEntity>? {
        return withContext(Dispatchers.IO) {
            roomDatabase?.getFavoriteRecipeDao()?.getFavoriteRecipes(idUser)
        }
    }

    suspend fun existInFavorites(id: Long, idUser: Long): Int? {
        return withContext(Dispatchers.IO) {
            roomDatabase?.getFavoriteRecipeDao()?.existInFavorites(id, idUser)
        }
    }
}