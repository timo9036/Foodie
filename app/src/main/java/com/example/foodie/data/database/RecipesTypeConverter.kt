package com.example.foodie.data.database

import androidx.room.TypeConverter
import com.example.foodie.dataclass.FoodRecipe
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecipesTypeConverter {

    var gson = Gson()

    @TypeConverter
    fun foodRecipeToString(foodRecipe: FoodRecipe): String{
        return gson.toJson(foodRecipe)
    }

    @TypeConverter
    fun stringToFoodRecipe(data:String): FoodRecipe{
        // creating an instance of TypeToken to specify the type that Gson should deserialize from the JSON string.
        // In this case, it's telling Gson to expect a FoodRecipe object.
        val listType = object : TypeToken<FoodRecipe>(){}.type
        return gson.fromJson(data,listType)
    }
}