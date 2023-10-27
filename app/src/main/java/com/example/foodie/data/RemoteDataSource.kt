package com.example.foodie.data

import com.example.foodie.data.network.FoodRecipesApi
import com.example.foodie.models.FoodRecipe
import retrofit2.Response
import javax.inject.Inject

//request data from api, inject recipesApi into remote data source
class RemoteDataSource @Inject constructor(
    private val foodRecipesApi: FoodRecipesApi
){

    suspend fun getRecipes(queries: Map<String, String>): Response<FoodRecipe> {
        return foodRecipesApi.getRecipes(queries)
    }

    suspend fun searchRecipes(searchQuery:Map<String, String>):Response<FoodRecipe>{
        return foodRecipesApi.searchRecipes(searchQuery)
    }

}