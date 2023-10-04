package com.example.foodie.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foodie.dataclass.FoodRecipe
import com.example.foodie.util.Constants.Companion.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
//defines a class RecipesEntity. The class has a primary constructor with a single parameter, foodRecipe, which is of type FoodRecipe
class RecipesEntity(
    var foodRecipe: FoodRecipe
) {
    @PrimaryKey(autoGenerate = false)
    //represents a unique identifier for each recipe entry in the database, by default an uninitialized RecipesEntity has an ID of 0
    var id: Int = 0
}