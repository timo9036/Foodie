package com.example.foodie.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.foodie.data.database.entities.FavoritesEntity
import com.example.foodie.data.database.entities.RecipesEntity

@Database(
    entities = [RecipesEntity::class, FavoritesEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(RecipesTypeConverter::class)
abstract class RecipesDatabase: RoomDatabase() {

    abstract  fun recipesDao(): RecipesDao
}
//to access database in the application, you create (or get) an instance of RecipesDatabase
// and then call the recipesDao() method to get access to the data access methods defined in the RecipesDao interface