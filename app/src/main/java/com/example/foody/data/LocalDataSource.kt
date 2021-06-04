package com.example.foody.data

import android.content.Context
import androidx.room.Room
import com.example.foody.data.database.*
import com.example.foody.models.FoodJoke
import com.example.foody.util.Constants.Companion.DATABASE_NAME
import kotlinx.coroutines.flow.Flow

class LocalDataSource(context: Context) {

    private val database = Room.databaseBuilder(context,
                            RecipesDatabase::class.java,
                        DATABASE_NAME).build()

    private val recipesDao: RecipesDao = database.recipesDao()

    fun readRecipes():Flow<List<RecipesEntity>>{
        return recipesDao.readRecipes()
    }

    fun readFavoriteRecipes() : Flow<List<FavoritesEntity>>{
        return recipesDao.readFavoriteRecipes()
    }

    fun readFoodJoke(): Flow<List<FoodJokeEntity>>{
        return recipesDao.readFoodJoke()
    }

    suspend fun insertRecipes(recipesEntity: RecipesEntity) {
        recipesDao.insertRecipes(recipesEntity)
    }

    suspend fun insertFavoriteRecipes(favoritesEntity: FavoritesEntity){
        recipesDao.insertFavoriteRecipes(favoritesEntity)
    }

    suspend fun insertFoodJoke(foodJokeEntity: FoodJokeEntity){
        recipesDao.insertFoodJoke(foodJokeEntity)
    }

    suspend fun deleteFavoriteRecipe(favoritesEntity: FavoritesEntity){
        recipesDao.deleteFavoriteRecipe(favoritesEntity)
    }

    suspend fun deleteAllFavoriteRecipes(){
        recipesDao.deleteAllFavoriteRecipes()
    }
}