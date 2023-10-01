package com.example.foodie.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodie.R
import com.example.foodie.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.repoLink.setOnClickListener {
            val url: Uri = Uri.parse("https://github.com/timo9036/Foodie")
            val intent = Intent(Intent.ACTION_VIEW, url)
            if (intent.resolveActivity(requireContext().packageManager) != null) {
                startActivity(intent)
            }
        }

        binding.linkedInLink.setOnClickListener {
            val recipeUrl: Uri = Uri.parse("https://www.linkedin.com/in/timothy-l-338875249/")
            val intent = Intent(Intent.ACTION_VIEW, recipeUrl)
            if (intent.resolveActivity(requireContext().packageManager) != null) {
                startActivity(intent)
            }
        }

        binding.githubLink.setOnClickListener {
            val recipeUrl: Uri = Uri.parse("https://github.com/timo9036")
            val intent = Intent(Intent.ACTION_VIEW, recipeUrl)
            if (intent.resolveActivity(requireContext().packageManager) != null) {
                startActivity(intent)
            }
        }

        binding.mailLink.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            val data = Uri.parse(
                "mailto:timo9036@hotmail.com?subject=" + Uri.encode("Regarding Foodie:")
            )
            intent.data = data
            startActivity(intent)
        }

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}