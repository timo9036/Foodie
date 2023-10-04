package com.example.foodie.bindingadapters

import android.content.Context
import android.graphics.PorterDuff
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.example.foodie.R
import com.example.foodie.ui.recipes.RecipesFragmentDirections
import java.lang.Exception
import com.example.foodie.dataclass.Result
import com.example.foodie.ui.SearchFragmentDirections
import org.jsoup.Jsoup

class RecipesItemBinding {

    companion object {

        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, imageUrl: String) {
//            Log.i("Tim", "imageUrl: $imageUrl")
            imageView.load(imageUrl) {
//                crossfade(300)
                error(R.drawable.error)
            }
        }

        @BindingAdapter("applyVeganColorMain")
        @JvmStatic
        fun applyVeganColorMain(view: TextView, vegan: Boolean) {
            if (vegan) {
                // Set text color to green
                view.setTextColor(ContextCompat.getColor(view.context, R.color.green))

                // Set drawable color to green
                val drawable = ContextCompat.getDrawable(view.context, R.drawable.ic_leaf)?.mutate()
                drawable?.setColorFilter(
                    ContextCompat.getColor(view.context, R.color.green),
                    PorterDuff.Mode.SRC_IN
                )
                view.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
            }
        }

        @BindingAdapter("applyVeganColor")
        @JvmStatic
        fun applyVeganColor(view: View, vegan: Boolean) {
            if (vegan) {
                when (view) {
                    is TextView -> {
                        view.setTextColor(
                            ContextCompat.getColor(
                                view.context,
                                R.color.green
                            )
                        )
                    }

                    is ImageView -> {
                        view.setColorFilter(
                            ContextCompat.getColor(
                                view.context,
                                R.color.green
                            )
                        )
                    }
                }
            }
        }

        @BindingAdapter("onRecipeClickListener")
        @JvmStatic
        fun onRecipeClickListener(recipeRowLayout: ConstraintLayout, result: Result) {
            Log.d("onRecipeClickListener", "CALLED")
            recipeRowLayout.setOnClickListener {
                try {
                    val navController = recipeRowLayout.findNavController()
                    when (navController.currentDestination?.id) {
                        R.id.recipesFragment -> {
                            val action1 = RecipesFragmentDirections.actionRecipesFragmentToDetailsActivity(result)
                            navController.navigate(action1)
                        }
                        R.id.searchFragment -> {
                            val action2 = SearchFragmentDirections.actionSearchFragmentToDetailsActivity(result)
                            navController.navigate(action2)
                        }
                    }
                } catch (e: Exception) {
                    Log.d("onRecipeClickListener", e.toString())
                }
            }
        }

        @BindingAdapter("parseHtml")
        @JvmStatic
        fun parseHtml(textView: TextView, description: String?){
            if(description != null){
                val desc = Jsoup.parse(description).text()
                textView.text = desc
            }
        }
    }

}