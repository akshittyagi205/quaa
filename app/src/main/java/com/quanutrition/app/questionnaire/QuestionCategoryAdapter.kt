package com.quanutrition.app.questionnaire

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.quanutrition.app.R
import com.quanutrition.app.Utils.OnClickListener
import com.quanutrition.app.databinding.QuestionCategoryListItemBinding
import java.util.*

class QuestionCategoryAdapter(private val list: ArrayList<QuestionCategoryModel>, private val mCtx: Context) : RecyclerView.Adapter<QuestionCategoryAdapter.MyViewHolder>() {

    lateinit var binding : QuestionCategoryListItemBinding
    var clickListener : OnClickListener?=null

    fun setOnClickListener(clickListener: OnClickListener){
        this.clickListener = clickListener
    }

    inner class MyViewHolder(view: View?) : RecyclerView.ViewHolder(view!!) {
        var icon = binding.icon
        var name = binding.name
        var layout = binding.layoutBack
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        binding = QuestionCategoryListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        val itemView = binding.root
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = list[position]
        if(model.imageId!=0){
            holder.icon.setImageResource(model.imageId)
        }

        holder.name.text = model.name

        holder.layout.setOnClickListener {
            clickListener?.onClick(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


}