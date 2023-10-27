package com.example.foodie.data

import android.content.Context
import android.preference.Preference
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.*
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.foodie.data.DataStoreRepository.PreferenceKeys.selectedMealTypeId
import com.example.foodie.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.example.foodie.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.example.foodie.util.Constants.Companion.PREFERENCES_BACK_ONLINE
import com.example.foodie.util.Constants.Companion.PREFERENCES_DIET_TYPE
import com.example.foodie.util.Constants.Companion.PREFERENCES_DIET_TYPE_ID
import com.example.foodie.util.Constants.Companion.PREFERENCES_MEAL_TYPE
import com.example.foodie.util.Constants.Companion.PREFERENCES_MEAL_TYPE_ID
import com.example.foodie.util.Constants.Companion.PREFERENCES_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

//by is an extension property added to the Android Context class. This allows you to access dataStore directly from any Context object
//a delegated property forwards its gets and sets to another property or function.
//returns a property delegate for a DataStore that saves its data as key-value pairs (preferences)
private val Context.dataStore by preferencesDataStore(PREFERENCES_NAME)

@ViewModelScoped
//Android application context
class DataStoreRepository @Inject constructor(@ApplicationContext private val context: Context) {

    //This is a singleton object inside the DataStoreRepository class. It defines the keys used to store and retrieve data from the DataStore
    private object PreferenceKeys {
        val selectedMealType = stringPreferencesKey(PREFERENCES_MEAL_TYPE)
        val selectedMealTypeId = intPreferencesKey(PREFERENCES_MEAL_TYPE_ID)
        val selectedDietType = stringPreferencesKey(PREFERENCES_DIET_TYPE)
        val selectedDietTypeId = intPreferencesKey(PREFERENCES_DIET_TYPE_ID)
        val backOnline = booleanPreferencesKey(PREFERENCES_BACK_ONLINE)
    }

    //an instance of DataStore Preferences used to save and retrieve data.
    //The context.dataStore is an extension function that provides access to the default instance of DataStore for preferences
    private val dataStore: DataStore<Preferences> = context.dataStore

    //allows app to save the meal and diet types to the DataStorePreference
    suspend fun saveMealAndDietType(
        mealType: String,
        mealTypeId: Int,
        dietType: String,
        dietTypeId: Int
    ) {
        //edit the current preferences. Inside its lambda, we set the values of the provided meal and diet types to their respective keys
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.selectedMealType] = mealType
            preferences[PreferenceKeys.selectedMealTypeId] = mealTypeId
            preferences[PreferenceKeys.selectedDietType] = dietType
            preferences[PreferenceKeys.selectedDietTypeId] = dietTypeId
        }
    }

    suspend fun saveBackOnline(backOnline:Boolean){
        dataStore.edit{ preferences ->
            preferences[PreferenceKeys.backOnline]= backOnline
        }
    }

    //read the stored datastore and emits MealAndDietType data stated below, represent a stream of data that can be collected
    val readMealAndDietType: Flow<MealAndDietType> = dataStore.data
        //if the exception is an IOException, it emits an empty set of preferences. For other exceptions, it just throws them to be handled upstream
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        //transforms the emitted preferences into a MealAndDietType object, pulling out the saved values or defaulting to predefined constants/values
        .map { preferences->
            val selectedMealType = preferences[PreferenceKeys.selectedMealType]?: DEFAULT_MEAL_TYPE
            val selectedMealTypeId = preferences[PreferenceKeys.selectedMealTypeId]?: 0
            val selectedDietType = preferences[PreferenceKeys.selectedDietType]?: DEFAULT_DIET_TYPE
            val selectedDietTypeId = preferences[PreferenceKeys.selectedDietTypeId]?: 0
            MealAndDietType(
                selectedMealType,
                selectedMealTypeId,
                selectedDietType,
                selectedDietTypeId
            )
        }

    val readBackOnline: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map {preferences ->
            val backOnline = preferences[PreferenceKeys.backOnline] ?: false
            backOnline
        }

}

data class MealAndDietType(
    val selectedMealType: String,
    val selectedMealTypeId: Int,
    val selectedDietType: String,
    val selectedDietTypeId: Int
)