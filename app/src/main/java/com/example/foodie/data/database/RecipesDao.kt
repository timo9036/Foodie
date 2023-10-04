package com.example.foodie.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.foodie.data.database.entities.FavoritesEntity
import com.example.foodie.data.database.entities.RecipesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipesDao {

    // if an item with the same primary key already exists, the old item will be replaced by the new one.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    //The method takes a single argument of type RecipesEntity, which is the item to be inserted into the database.
    suspend fun insertRecipes(recipesEntity: RecipesEntity)

    // fetches all entries from the recipes_table, ordering them in ascending order based on their id
    @Query("SELECT * FROM recipes_table ORDER BY id ASC")
    //returns a Flow of a list of RecipesEntity. Flow is a type from the Kotlin Coroutines library representing a cold asynchronous data stream.
    // It's cold in the sense that the data is only produced (or the query is only run) when there's a collector actively listening.
    // This is useful for observing database changes and updating the UI or other components accordingly
    fun readRecipes(): Flow<List<RecipesEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteRecipe(favoritesEntity: FavoritesEntity)

    @Query("Select * From favorite_recipes_table ORDER BY id ASC")
    fun readFavoritesRecipes():Flow<List<FavoritesEntity>>

    @Delete
    suspend fun deleteFavoriteRecipe(favoritesEntity: FavoritesEntity)

    @Query("DELETE FROM favorite_recipes_table")
    suspend fun deleteAllFavoriteRecipes()
}