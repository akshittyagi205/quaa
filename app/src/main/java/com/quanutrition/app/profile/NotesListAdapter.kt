package com.quanutrition.app.profile;

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.quanutrition.app.databinding.NotesListItemBinding

class NotesListAdapter : RecyclerView.Adapter<NotesListAdapter.ViewHolder>() {
    var data = ArrayList<NotesModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    interface OnClickListener {
        fun onClick(position: Int)
    }
    var clickListener: OnClickListener? = null

    class ViewHolder private constructor(val binding: NotesListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = NotesListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

        fun bind(model: NotesModel, clickListener: OnClickListener?) {
            binding.title.text = model.title
            binding.date.text = model.date

            binding.back.setOnClickListener {
                clickListener?.onClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = data[position]
        holder.bind(model, clickListener)
    }

    override fun getItemCount() = data.size
}