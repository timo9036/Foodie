package com.example.foodie.ui.viewpager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodie.R
import com.example.foodie.adapters.IngredientsAdapter
import com.example.foodie.databinding.FragmentIngredientsBinding
import com.example.foodie.util.Constants.Companion.RECIPE_RESULT_KEY
import com.example.foodie.util.retrieveParcelable
import com.example.foodie.dataclass.Result


class IngredientsFragment : Fragment() {

    private val myAdapter: IngredientsAdapter by lazy { IngredientsAdapter() }

    private var _binding: FragmentIngredientsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentIngredientsBinding.inflate(inflater, container, false)

        val args = arguments
        val myBundle: Result? = args?.retrieveParcelable(RECIPE_RESULT_KEY)

        setupRecyclerView()
        myBundle?.extendedIngredients?.let { myAdapter.setData(it) }

        return binding.root
    }

    private fun setupRecyclerView(){
        binding.ingredientsRecyclerview.adapter = myAdapter
        binding.ingredientsRecyclerview.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}