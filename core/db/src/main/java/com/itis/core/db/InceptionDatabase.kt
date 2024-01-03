package com.itis.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.itis.core.db.dao.FavoriteRecipeDao
import com.itis.core.db.dao.UserDao
import com.itis.core.db.entity.FavoriteRecipeEntity
import com.itis.core.db.entity.UserEntity

@Database(entities = [UserEntity::class, FavoriteRecipeEntity::class], version = DatabaseHandler.versiondb)
abstract class InceptionDatabase: RoomDatabase() {

    abstract fun getUserDao(): UserDao
    abstract fun getFavoriteRecipeDao(): FavoriteRecipeDao

}
