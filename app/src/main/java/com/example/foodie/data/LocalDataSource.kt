package com.example.foodie.data

import com.example.foodie.data.database.entities.FavoritesEntity
import com.example.foodie.data.database.RecipesDao
import com.example.foodie.data.database.entities.RecipesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

//create instances of LocalDataSource and that it requires a RecipesDao as a dependency.
//When ask Dagger for an instance of LocalDataSource, Dagger will automatically provide the required RecipesDao instance
class LocalDataSource @Inject constructor(
    private val recipesDao:RecipesDao
){
    //represents an asynchronous stream of data, a list of RecipesEntity
    fun readRecipes(): Flow<List<RecipesEntity>> {
        return recipesDao.readRecipes()
    }

    //recipesEntity,data to be inserted into the database.
    suspend fun insertRecipes(recipesEntity: RecipesEntity){
        recipesDao.insertRecipes(recipesEntity)
    }

    fun readFavoriteRecipes():Flow<List<FavoritesEntity>>{
        return recipesDao.readFavoritesRecipes()
    }

    suspend fun insertFavoriteRecipes(favoritesEntity: FavoritesEntity){
        recipesDao.insertFavoriteRecipe(favoritesEntity)
    }

    suspend fun deleteFavoriteRecipe(favoritesEntity: FavoritesEntity){
        recipesDao.deleteFavoriteRecipe(favoritesEntity)
    }

    suspend fun deleteAllFavoriteRecipes() {
        recipesDao.deleteAllFavoriteRecipes()
    }

}