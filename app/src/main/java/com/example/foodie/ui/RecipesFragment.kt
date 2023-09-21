package com.example.foodie.ui

import android.net.Network
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodie.R
import com.example.foodie.viewmodels.MainViewModel
import com.example.foodie.adapters.RecipesAdapter
import com.example.foodie.databinding.FragmentRecipesBinding
import com.example.foodie.util.Constants.Companion.API_KEY
import com.example.foodie.util.NetworkResult
import com.example.foodie.util.observeOnce
import com.example.foodie.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipesFragment : Fragment() {

    //by keyword denotes property delegation in Kotlin.
    //Instead of the args property holding a value itself, it delegates its get (and potentially set) operations to another object or function
    //allows you to retrieve the arguments passed to this fragment in a type-safe manner.
    //The generic type RecipesFragmentArgs is a generated class that provides access to the arguments
    private val args by navArgs<RecipesFragmentArgs>()

    //hold an instance of the binding class or be null. It's initialized as null and is private to ensure that outside classes can't access or modify it directly
    //By using the !! operator, it will throw a KotlinNullPointerException if _binding is null.
    //This is useful because, when accessing views, you typically want to be sure that the binding is available.
    //If it's not, it indicates a programmer error (like accessing the views after onDestroy).
    private var _binding: FragmentRecipesBinding? = null
    //This is a read-only property that provides a non-nullable version of _binding
    private val binding get() = _binding!!

    private val myAdapter by lazy { RecipesAdapter() }
    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipesViewModel: RecipesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        recipesViewModel = ViewModelProvider(requireActivity())[RecipesViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        //This assigns the fragment as the lifecycle owner for the binding.
        //This allows LiveData used in data binding expressions to automatically observe changes without manually setting up observers in the fragment
        binding.lifecycleOwner = this
        //This sets a property called mainViewModel on the binding.
        //used in the XML layout file to bind UI components to data and functions provided by mainViewModel
        binding.mainViewModel = mainViewModel

        setupRecyclerView()
//        requestApiData()
        readDatabase()

        binding.recipesFab.setOnClickListener {
            findNavController().navigate(R.id.action_recipesFragment_to_recipesBottomSheet)
        }
        //This returns the root view of the binding class.
        //this line would be in the onCreateView method of a fragment, where you inflate the layout and return the root view to be displayed
        return binding.root
    }

    private fun setupRecyclerView() {
        binding.recyclerview.adapter = myAdapter
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            //observing changes on readRecipes, which is a LiveData in mainViewModel.
            //Every time this LiveData updates, the lambda passed to observe will execute
            mainViewModel.readRecipes.observeOnce(viewLifecycleOwner) { database ->
                //If the Fragment was not navigated to after closing a BottomSheet, as indicated by the backFromBottomSheet argument passed to the Fragment
                if (database.isNotEmpty() && !args.backFromBottomSheet){
                    Log.d("api", "requestDatabase")
                    //The data for the first recipe from the database is set on myAdapter
                    myAdapter.setData(database.first().foodRecipe)
                    hideShimmerEffect()
                } else {
                    requestApiData()
                }
            }
        }
    }

    private fun requestApiData() {
        Log.d("api", "requestApi")
        mainViewModel.getRecipes(recipesViewModel.applyQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { myAdapter.setData(it) }
                }

                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        }
    }

    private fun loadDataFromCache() {
        //observes the readRecipes LiveData from mainViewModel
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

    //When a fragment is being destroyed, it's a good practice to set _binding to null.
    //This helps in preventing memory leaks by allowing the binding to be garbage collected.
    //By doing this, you ensure that you don't accidentally reference views after the fragment's view has been destroyed
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

