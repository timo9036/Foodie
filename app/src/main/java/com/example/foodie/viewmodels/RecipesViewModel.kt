package com.example.foodie.viewmodels

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodie.data.DataStoreRepository
import com.example.foodie.data.MealAndDietType
import com.example.foodie.util.Constants.Companion.FOOD_API_KEY
import com.example.foodie.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.example.foodie.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.example.foodie.util.Constants.Companion.DEFAULT_RECIPES_NUMBER
import com.example.foodie.util.Constants.Companion.QUERY_ADD_NUTRITION
import com.example.foodie.util.Constants.Companion.QUERY_ADD_RECIPE_INFORMATION
import com.example.foodie.util.Constants.Companion.QUERY_API_KEY
import com.example.foodie.util.Constants.Companion.QUERY_DIET
import com.example.foodie.util.Constants.Companion.QUERY_FILL_INGREDIENTS
import com.example.foodie.util.Constants.Companion.QUERY_INSTRUCTION_REQUIRED
import com.example.foodie.util.Constants.Companion.QUERY_NUMBER
import com.example.foodie.util.Constants.Companion.QUERY_SEARCH
import com.example.foodie.util.Constants.Companion.QUERY_SORT
import com.example.foodie.util.Constants.Companion.QUERY_SORT_DIRECTION
import com.example.foodie.util.Constants.Companion.QUERY_TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {

    private lateinit var mealAndDiet: MealAndDietType

    private var mealType = DEFAULT_MEAL_TYPE
    private var dietType = DEFAULT_DIET_TYPE

    var networkStatus = false
    var backOnline = false

    //reference to the readMealAndDietType from the DataStoreRepository. It's a Flow that will emit saved preferences as MealAndDietType objects
    val readMealAndDietType = dataStoreRepository.readMealAndDietType
    val readBackOnline = dataStoreRepository.readBackOnline.asLiveData()

    //saves the meal and diet type using the DataStoreRepository,
    //invokes the saveMealAndDietType function of the dataStoreRepository to actually save the provided data
    fun saveMealAndDietType() =
        viewModelScope.launch(Dispatchers.IO) {
            if (this@RecipesViewModel::mealAndDiet.isInitialized) {
                dataStoreRepository.saveMealAndDietType(
                    mealAndDiet.selectedMealType,
                    mealAndDiet.selectedMealTypeId,
                    mealAndDiet.selectedDietType,
                    mealAndDiet.selectedDietTypeId
                )
            }
        }

    fun saveMealAndDietTypeTemp(
        mealType: String,
        mealTypeId: Int,
        dietType: String,
        dietTypeId: Int
    ) {
        mealAndDiet = MealAndDietType(
            mealType,
            mealTypeId,
            dietType,
            dietTypeId
        )
    }

    private fun saveBackOnline(backOnline:Boolean)=
        viewModelScope.launch (Dispatchers.IO) {
            dataStoreRepository.saveBackOnline(backOnline)
        }

    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_API_KEY] = FOOD_API_KEY
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        if (this@RecipesViewModel::mealAndDiet.isInitialized) {
            queries[QUERY_TYPE] = mealAndDiet.selectedMealType
            queries[QUERY_DIET] = mealAndDiet.selectedDietType
        } else {
            queries[QUERY_TYPE] = DEFAULT_MEAL_TYPE
            queries[QUERY_DIET] = DEFAULT_DIET_TYPE
        }

        return queries
    }

    fun applyRandomQueries(context: Context, recipeNumber: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        viewModelScope.launch {
            readMealAndDietType.collect { values ->
                mealType = values.selectedMealType
                dietType = values.selectedDietType
            }
        }

        val sharedPreference =
            context.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        mealType = sharedPreference.getString("meal", DEFAULT_MEAL_TYPE).toString()
        dietType = sharedPreference.getString("diet", DEFAULT_DIET_TYPE).toString()
        queries[QUERY_NUMBER] = recipeNumber
        queries[QUERY_API_KEY] = FOOD_API_KEY
        if (recipeNumber != "5") {
            queries[QUERY_TYPE] = mealType
            queries[QUERY_DIET] = dietType
        }
        queries[QUERY_INSTRUCTION_REQUIRED] = "true"
        queries[QUERY_SORT] = "popularity"
        queries[QUERY_SORT_DIRECTION] = "asc"
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"
        queries[QUERY_ADD_NUTRITION] = "true"

        return queries
    }

    fun applySearchQuery(searchQuery:String): HashMap<String,String>{
        val queries: HashMap<String, String> = HashMap()
        queries[QUERY_SEARCH]= searchQuery
        queries[QUERY_NUMBER]= DEFAULT_RECIPES_NUMBER
        queries[QUERY_API_KEY]= FOOD_API_KEY
        queries[QUERY_ADD_RECIPE_INFORMATION]= "true"
        queries[QUERY_FILL_INGREDIENTS]= "true"
        return queries
    }

    fun showNetworkStatus(){
        if (!networkStatus){
            Toast.makeText(getApplication(), "No Internet Connection.", Toast.LENGTH_SHORT).show()
            saveBackOnline(true)
        } else if (networkStatus) {
            if (backOnline) {
                Toast.makeText(getApplication(), "We're back online.", Toast.LENGTH_SHORT).show()
                saveBackOnline(false)
            }
        }
    }
}