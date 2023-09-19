package com.example.foodie.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [RecipesEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(RecipesTypeConverter::class)
abstract class RecipesDatabase: RoomDatabase() {

    abstract  fun recipesDao(): RecipesDao
}
//to access database in the application, you create (or get) an instance of RecipesDatabase
// and then call the recipesDao() method to get access to the data access methods defined in the RecipesDao interface