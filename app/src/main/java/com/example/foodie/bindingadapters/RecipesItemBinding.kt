package com.example.foodie.bindingadapters
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.foodie.R
import com.example.foodie.models.ExtendedIngredient
import com.example.foodie.models.Nutrition
import com.example.foodie.ui.recipes.RecipesFragmentDirections
import java.lang.Exception
import com.example.foodie.models.Result
import com.example.foodie.ui.SearchFragmentDirections
import org.jsoup.Jsoup
import androidx.palette.graphics.Palette
import androidx.palette.graphics.Palette.Swatch
import java.util.Collections

class RecipesItemBinding {

    companion object {

        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, imageUrl: String) {
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
//                        R.id.recipesFragment -> {
//                            val action1 = RecipesFragmentDirections.actionRecipesFragmentToDetailsActivity(result)
//                            navController.navigate(action1)
//                        }
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

        //detail page
        @BindingAdapter("android:loadNoOfIngredients")
        @JvmStatic
        fun loadNoOfIngredients(textView: TextView, ingredients: List<ExtendedIngredient?>?) {
            val txt = ingredients?.size.toString() + " ingredients"
            textView.text = txt
        }

        @BindingAdapter("android:setMinutes")
        @JvmStatic
        fun setMinutes(textView: TextView, minutes: Int) {
            val minutesTxt = "$minutes min"
            textView.text = minutesTxt
        }

//        @BindingAdapter("android:loadCal")
//        @JvmStatic
//        fun loadCal(textView: TextView, nutrition: Nutrition) {
//            val txt =
//                nutrition.nutrients?.get(0)?.amount.toString() + " " + nutrition.nutrients?.get(0)?.unit
//
//            textView.text = txt
//        }

        @BindingAdapter("android:setServings")
        @JvmStatic
        fun setServing(textView: TextView, serving: Int) {
            val servings = "$serving servings"
            textView.text = servings
        }

        @BindingAdapter("android:setHTMLText")
        @JvmStatic
        fun setHTMLText(textView: TextView, overView: String?) {
            if (!overView.isNullOrEmpty()) {
                val htmlAsSpanned = Html.fromHtml(overView)
                textView.text = htmlAsSpanned
            }
        }

        @BindingAdapter("android:loadIngredientImageBackground")
        @JvmStatic
        fun loadBackGround(imageView: ImageView, imageUrl: String?) {
            Glide.with(imageView).asBitmap()
                .load("https://spoonacular.com/cdn/ingredients_100x100/$imageUrl")
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(object : RequestListener<Bitmap?> {
                    override fun onLoadFailed(
                        @Nullable e: GlideException?,
                        model: Any,
                        target: Target<Bitmap?>,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Bitmap?,
                        model: Any,
                        target: Target<Bitmap?>,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        val p: Palette = Palette.from(resource!!).generate()
                        val palette: Swatch = getDominantSwatch(p)
                        imageView.background = ContextCompat.getDrawable(
                            imageView.context,
                            R.drawable.background_ingredients
                        )
                        val drawable: GradientDrawable = imageView.background as GradientDrawable
                        drawable.setColor(palette.rgb)
                        drawable.cornerRadius = 20f
                        return true
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView)
        }
        private fun getDominantSwatch(palette: Palette): Swatch {
            // find most-represented swatch based on population
            return Collections.max(
                palette.swatches
            ) { sw1, sw2 -> sw1.population.compareTo(sw2.population) }
        }

        @BindingAdapter("android:loadIngredientImage")
        @JvmStatic
        fun loadIngredientImage(imageView: ImageView, imageUrl: String?) {
            Glide.with(imageView.context)
                .load("https://spoonacular.com/cdn/ingredients_100x100/$imageUrl")
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView)
        }

        @BindingAdapter("android:setAmount", "android:setUnit", requireAll = true)
        @JvmStatic
        fun loadMeasure(textView: TextView, amount: Double, unit: String) {
            var txt = ""
            if (unit.isEmpty()) {
                txt = "$amount items"
            } else {
                txt = "$amount $unit"
            }
            textView.isSelected = true
            textView.text = txt
        }

        @BindingAdapter("android:loadStepCount")
        @JvmStatic
        fun loadStepCount(textView: TextView, number: Int) {
            val txt = "Step $number"
            textView.text = txt
        }

        @BindingAdapter("android:loadLikes")
        @JvmStatic
        fun loadLikes(textView: TextView, number: Int) {
            val txt = "$number Likes"
            textView.text = txt
        }

//        @BindingAdapter("android:loadCal")
//        @JvmStatic
//        fun loadCal(textView: TextView, nutrition: Nutrition?) {
//            val txt =
//                nutrition?.nutrients?.get(0)?.amount.toString() + " " + nutrition?.nutrients?.get(0)?.unit
//
//            textView.text = txt
//        }

    }
}