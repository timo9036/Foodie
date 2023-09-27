package com.example.foodie.ui.viewpager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import coil.load
import com.example.foodie.R
import com.example.foodie.bindingadapters.RecipesItemBinding
import com.example.foodie.dataclass.Result
import com.example.foodie.databinding.FragmentOverviewBinding
import com.example.foodie.util.Constants.Companion.RECIPE_RESULT_KEY
import com.example.foodie.util.retrieveParcelable

class OverviewFragment : Fragment() {

    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)

        val args = arguments
        val myBundle: Result? = args!!.retrieveParcelable(RECIPE_RESULT_KEY) as Result?

        if (myBundle != null) {
            binding.mainImageView.load(myBundle.image)
            binding.titleTextView.text = myBundle.title
            RecipesItemBinding.parseHtml(binding.summaryTextView, myBundle.summary)

            updateColors(myBundle.vegetarian, binding.vegetarianTextView, binding.vegetarianImageView)
            updateColors(myBundle.vegan, binding.veganTextView, binding.veganImageView)
            updateColors(myBundle.cheap, binding.cheapTextView, binding.cheapImageView)
            updateColors(myBundle.dairyFree, binding.dairyFreeTextView, binding.dairyFreeImageView)
            updateColors(myBundle.glutenFree, binding.glutenFreeTextView, binding.glutenFreeImageView)
            updateColors(myBundle.veryHealthy, binding.healthyTextView, binding.healthyImageView)
        }

        return binding.root
    }

    private fun updateColors(stateIsOn: Boolean, textView: TextView, imageView: ImageView) {
        if (stateIsOn) {
            imageView.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}