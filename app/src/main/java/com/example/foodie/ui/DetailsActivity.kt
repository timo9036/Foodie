package com.example.foodie.ui

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodie.R
import com.example.foodie.adapters.IngredientsAdapter
import com.example.foodie.adapters.StepsAdapter
import com.example.foodie.data.database.entities.FavoritesEntity
import com.example.foodie.databinding.ActivityDetailsBinding
import com.example.foodie.models.Step
import com.example.foodie.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import com.example.foodie.models.Result
import kotlinx.parcelize.RawValue

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private lateinit var recipe: Result
    private lateinit var mainViewModel: MainViewModel

    private var recipeSaved = false
    private var savedRecipeId = 0
    private val ingredientsAdapter by lazy { IngredientsAdapter() }
    private val stepsAdapter by lazy { StepsAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recipe = intent.extras!!.getParcelable("result")!!
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.result = recipe

        binding.apply {
            window.apply {
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                statusBarColor = Color.TRANSPARENT
            }

            ingredientsRecyclerview.layoutManager =
                LinearLayoutManager(this@DetailsActivity, LinearLayoutManager.HORIZONTAL, false)
            ingredientsRecyclerview.adapter = ingredientsAdapter
            ingredientsAdapter.setData(recipe.extendedIngredients)

            stepsRecyclerView.layoutManager =
                LinearLayoutManager(this@DetailsActivity, LinearLayoutManager.VERTICAL, false)
            stepsRecyclerView.adapter = stepsAdapter
//            stepsAdapter.setData(recipe.analyzedInstructions?.get(0)?.steps as @RawValue List<Step>)
            val steps = recipe.analyzedInstructions?.getOrNull(0)?.steps
            if (steps != null) {
                stepsAdapter.setData(steps as? List<Step> ?: emptyList())
            } else {
                // Handle the case when steps are null or not a list
                stepsAdapter.setData(emptyList())
            }

            addToFavBtn.setOnClickListener {
                if (recipeSaved) {
                    removeFromFavorites(favIcon)
                } else {
                    saveToFavorites(favIcon)
                }
            }

            backBtn.setOnClickListener {
                finish()
            }
            checkSavedRecipes()
        }
    }

    private fun saveToFavorites(icon: ImageView) {
        val favoritesEntity = FavoritesEntity(
            0, recipe
        )
        mainViewModel.insertFavoriteRecipe(favoritesEntity)
        changeMenuItemColor(icon, R.color.red)
        showSnackBar("Recipe Saved")
        recipeSaved = true
    }

    private fun removeFromFavorites(icon: ImageView) {
        val favoritesEntity = FavoritesEntity(
            savedRecipeId,
            recipe
        )
        mainViewModel.deleteFavoriteRecipe(favoritesEntity)
        changeMenuItemColor(icon, R.color.black)
        showSnackBar("Removed from Favorites")
        recipeSaved = false
    }


    private fun checkSavedRecipes() {
        mainViewModel.readFavoriteRecipes.observe(this) { favoritesEntity ->
            try {
                for (savedRecipe in favoritesEntity) {
                    if (savedRecipe.result.id == recipe.id) {
                        changeMenuItemColor(binding.favIcon, R.color.red)
                        savedRecipeId = savedRecipe.id
                        recipeSaved = true
                    }
                }
            } catch (e: Exception) {
                Log.d("DetailsActivity", e.message.toString())
            }
        }
    }



    private fun showSnackBar(message: String) {
        Snackbar.make(
            binding.detailsLayout, message, Snackbar.LENGTH_SHORT
        ).setAction("Okay") {}
            .show()
    }

    private fun changeMenuItemColor(icon: ImageView, color: Int) {
        icon.setColorFilter(ContextCompat.getColor(this, color))
    }

}