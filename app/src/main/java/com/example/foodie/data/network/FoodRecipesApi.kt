package com.example.foodie.data.network

import com.example.foodie.models.FoodRecipe
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface FoodRecipesApi {

    @GET("/recipes/complexSearch")
    //suspend for coroutines, run on background thread
    suspend fun getRecipes(
        //add all queries into hashmap,key value
        @QueryMap queries: Map<String, String>
        //retrofit lib for http response
    ): Response<FoodRecipe>

    @GET("/recipes/complexSearch")
    suspend fun searchRecipes(
        @QueryMap searchQuery: Map<String, String>
    ):Response<FoodRecipe>

}