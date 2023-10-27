package com.example.foodie.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foodie.databinding.ItemDetailsStepsBinding
import kotlinx.android.parcel.RawValue
import com.example.foodie.models.Step
import com.example.foodie.util.RecipesDiffUtil


class StepsAdapter : RecyclerView.Adapter<StepsAdapter.StepsViewHolder>() {

    private var steps = emptyList<Step>()

    class StepsViewHolder(private val binding: ItemDetailsStepsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(result: Step) {
            binding.step = result
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): StepsViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemDetailsStepsBinding.inflate(layoutInflater, parent, false)
                return StepsViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StepsViewHolder {
        return StepsViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: StepsViewHolder, position: Int) {
        val current = steps[position]
        holder.bind(current)
    }

    override fun getItemCount(): Int {
        return steps.size
    }

    fun setData(newData: @RawValue List<Step>) {
        val recipesDiffUtil = RecipesDiffUtil(steps, newData)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        steps = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }

}