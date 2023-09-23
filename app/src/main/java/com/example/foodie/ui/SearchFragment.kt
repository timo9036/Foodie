package com.example.foodie.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.foodie.R
import com.example.foodie.adapters.RecipesAdapter
import com.example.foodie.databinding.FragmentRecipesBinding
import com.example.foodie.databinding.FragmentSearchBinding
import com.example.foodie.util.NetworkResult
import com.example.foodie.viewmodels.MainViewModel
import com.example.foodie.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipesViewModel: RecipesViewModel
    private val myAdapter by lazy { RecipesAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        recipesViewModel = ViewModelProvider(this).get(RecipesViewModel::class.java)

        binding.apply {
            recyclerview.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

            recyclerview.adapter = myAdapter

            searchBtn.setOnClickListener {
                searchView.visibility = View.VISIBLE
                searchView.isIconified = false
                searchBtn.visibility = View.INVISIBLE
                textView.visibility = View.INVISIBLE

            }

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String): Boolean {
                    return true
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    searchApiData(query)
                    return true
                }
            })

            val clearButton: ImageView =
                searchView.findViewById(androidx.appcompat.R.id.search_close_btn)
            clearButton.setOnClickListener { v ->
                if (searchView.query.isEmpty()) {
                    searchView.isIconified = true
                    searchView.visibility = View.INVISIBLE
                    searchBtn.visibility = View.VISIBLE
                    textView.visibility = View.VISIBLE
                } else {
                    // Do your task here
                    searchView.setQuery("", false)
                }
            }
        }
        return binding.root
    }

    private fun searchApiData(searchQuery: String) {
        showShimmerEffect()
        mainViewModel.searchRecipes(recipesViewModel.applySearchQuery(searchQuery))
        mainViewModel.searchedRecipesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    binding.animationView.visibility = View.INVISIBLE
                    binding.subText.visibility = View.INVISIBLE
                    val foodRecipe = response.data
                    foodRecipe?.let { myAdapter.setData(it) }
                }

                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    binding.animationView.visibility = View.VISIBLE
                    binding.subText.visibility = View.VISIBLE
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is NetworkResult.Loading -> {
                    showShimmerEffect()
                    binding.animationView.visibility = View.INVISIBLE
                    binding.subText.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun showShimmerEffect() {
        binding.shimmerFrameLayout.startShimmer()
        binding.shimmerFrameLayout.visibility = View.VISIBLE
        binding.recyclerview.visibility = View.GONE
    }

    private fun hideShimmerEffect() {
        binding.shimmerFrameLayout.stopShimmer()
        binding.shimmerFrameLayout.visibility = View.GONE
        binding.recyclerview.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
