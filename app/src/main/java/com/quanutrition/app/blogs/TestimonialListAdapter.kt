package com.quanutrition.app.blogs;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.quanutrition.app.Utils.Tools
import com.quanutrition.app.databinding.TestimonialListItemBinding

class TestimonialListAdapter : RecyclerView.Adapter<TestimonialListAdapter.ViewHolder>() {
    var data = ArrayList<TestimonialModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    interface OnClickListener {
        fun onClick(position: Int)
    }

    var clickListener: OnClickListener? = null

    class ViewHolder private constructor(val binding: TestimonialListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TestimonialListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

        fun bind(model: TestimonialModel, clickListener: OnClickListener?) {

            binding.title.text = model.title
            Tools.setHTMLData(binding.content,model.content)
            if (!model.user.isNullOrEmpty()){
                binding.user.text = "By : ${model.user}"
            }
            binding.addedOn.text = "Published On : ${model.addedOn}"

            binding.lytParent.setOnClickListener {
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