package com.example.foodie.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.foodie.R
import com.example.foodie.adapters.RecipesAdapter
import com.example.foodie.databinding.FragmentRecipesBinding
import com.example.foodie.databinding.FragmentSearchBinding
import com.example.foodie.util.NetworkResult
import com.example.foodie.viewmodels.MainViewModel
import com.example.foodie.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle

@AndroidEntryPoint
class SearchFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipesViewModel: RecipesViewModel
    private val myAdapter by lazy { RecipesAdapter() }

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var searchJob: Job? = null


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

            val menuHost: MenuHost = requireActivity()
            menuHost.addMenuProvider(object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.recipes_menu, menu)

                    val search = menu.findItem(R.id.menu_search)
                    val searchView = search.actionView as? SearchView
                    searchView?.isSubmitButtonEnabled = true
                    searchView?.setOnQueryTextListener(this@SearchFragment)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return true
                }
            }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        }
        return binding.root
    }


    override fun onQueryTextChange(newText: String): Boolean {
        binding.recipesImage.visibility = View.INVISIBLE
        searchJob?.cancel()
        searchJob = coroutineScope.launch {
            delay(600L)
            if (newText.isNotBlank()) {
                searchApiData(newText)
            }
        }
        return true
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        binding.recipesImage.visibility = View.INVISIBLE
        searchApiData(query)
        return true
    }

    private fun searchApiData(searchQuery: String) {
        showShimmerEffect()
        mainViewModel.searchRecipes(recipesViewModel.applySearchQuery(searchQuery))
        mainViewModel.searchedRecipesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    binding.recipesImage.visibility = View.INVISIBLE
                    val foodRecipe = response.data
                    foodRecipe?.let { myAdapter.setData(it) }
                }

                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
                    binding.recipesImage.visibility = View.INVISIBLE
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is NetworkResult.Loading -> {
                    showShimmerEffect()
                    binding.recipesImage.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun loadDataFromCache() {
        mainViewModel.readRecipes.observe(viewLifecycleOwner) { database ->
            if (database.isNotEmpty()) {
                myAdapter.setData(database.first().foodRecipe)
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
        coroutineScope.cancel()
    }

}
