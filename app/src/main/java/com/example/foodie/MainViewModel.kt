package com.example.foodie

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodie.data.Repository
import com.example.foodie.dataclass.FoodRecipe
import com.example.foodie.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    //hold and observe the results of network requests of type NetworkResult<FoodRecipe>,contain different states such as Success, Error, or Loading
    var recipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    //initiate a network request for recipes
    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesSafeCall(queries)
    }

    //Sets the recipesResponse to NetworkResult.Loading() to indicate that the request is in progress.
    //Checks if there is an internet connection using the hasInternetConnection function.
    //If there is an internet connection, it makes a network request using the repository.remote.getRecipes(queries) call
    // and handles the response using the handleFoodRecipesResponse function.
    //If there is no internet connection, it sets recipesResponse to NetworkResult.Error("No Internet Connection").
    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        recipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getRecipes(queries)
                recipesResponse.value = handleFoodRecipesResponse(response)
            } catch (e: Exception) {
                recipesResponse.value = NetworkResult.Error("Recipes not found")
            }
        } else {
            recipesResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    //This private function processes the response from the network request.
    // It checks various conditions based on the HTTP response, such as timeouts, error codes, and empty responses, and maps them to NetworkResult states.
    private fun handleFoodRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe>? {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API Key Limited")
            }
            response.body()!!.results.isNullOrEmpty() -> {
                return NetworkResult.Error("Recipes not found")
            }
            //If successful, it retrieves the FoodRecipe object from the response body and returns a NetworkResult.Success instance with the FoodRecipe data
            response.isSuccessful -> {
                val foodRecipes = response.body()
                return NetworkResult.Success(foodRecipes!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

//checks the active network's capabilities and transport type to determine the type of internet connection.
    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        //obtains the currently active network connection, false if activeNetwork is null;no network
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        //obtains the network capabilities for the active network connection, if capabilities is null return false; no internet connection
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}