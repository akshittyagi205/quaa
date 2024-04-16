package com.quanutrition.app.general;

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.quanutrition.app.databinding.NotificationListItemBinding

class NotificationAdapter : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {
    var data = ArrayList<NotificationModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    interface OnClickListener {
        fun onClick(position: Int)
    }
    var clickListener: OnClickListener? = null

    class ViewHolder private constructor(val binding: NotificationListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = NotificationListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

        fun bind(model: NotificationModel, clickListener: OnClickListener?) {
            binding.title.text = model.title
            binding.text.text = model.text
            binding.date.text = model.timestamp
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