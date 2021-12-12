package com.quanutrition.app.dietextras

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.quanutrition.app.databinding.IngredientListItemBinding

class IngredientsAdapter : RecyclerView.Adapter<IngredientsAdapter.ViewHolder>(){

    var data = ArrayList<IngredientsModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun getItemCount() = data.size

    class ViewHolder(val binding: IngredientListItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: IngredientsModel) {
            binding.name.text = item.name
            binding.quantity.text = item.quantity +" gm"
            binding.head.setOnClickListener {
                if(item.open){
                    item.open = false
                    binding.details.visibility = View.GONE
                }else{
                    item.open = true
                    binding.details.visibility = View.VISIBLE
                }
            }
            binding.expand.visibility = View.GONE
            binding.cal.text = item.cal+" KCal."
            binding.fat.text = item.fat+" gm"
            binding.carbs.text = item.carbs+" gm"
            binding.pro.text = item.protien+" gm"

            if(item.open){
                binding.details.visibility = View.VISIBLE
            }else{
                binding.details.visibility = View.GONE
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = IngredientListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

}