package com.example.foodie.di

import android.content.Context
import androidx.room.Room
import com.example.foodie.data.database.RecipesDatabase
import com.example.foodie.util.Constants.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDataBase(
        //the provided context will be application-wide, rather than activity- or fragment-specific
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        RecipesDatabase::class.java,
        DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    //provides the RecipesDao instance. It takes a RecipesDatabase instance as a parameter
    //recipesDao() method on the provided database instance, which returns the DAO
    fun provideDao(database: RecipesDatabase) = database.recipesDao()
}

//DatabaseModule is an object that uses Dagger (or Hilt) annotations to provide database-related dependencies to the rest of the app.
//It provides a RecipesDatabase instance and a RecipesDao instance. By using annotations and Dagger's DI mechanism,
//other parts of the app can request these dependencies without needing to know how they're constructed,
//promoting a separation of concerns and making the code more maintainable and testable.