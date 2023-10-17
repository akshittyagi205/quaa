package com.quanutrition.app.dietextras

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.quanutrition.app.databinding.SupplementListItemBinding
import com.quanutrition.app.diet.MealAdapter

class SupplementsAdapter(val context : Context) : RecyclerView.Adapter<SupplementsAdapter.ViewHolder>(){

    var data = ArrayList<SupplementModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item,context)
    }

    override fun getItemCount() = data.size

    class ViewHolder(val binding: SupplementListItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: SupplementModel, context: Context) {
            binding.dayName.text = item.day
            val adapter = MealAdapter(item.list,context,false)
//            adapter.setDefaultOpen(true)
            binding.re.adapter = adapter
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SupplementListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

}