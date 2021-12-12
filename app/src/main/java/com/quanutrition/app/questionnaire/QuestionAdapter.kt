package com.quanutrition.app.questionnaire

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.quanutrition.app.R
import com.quanutrition.app.databinding.QuestionListItemBinding
import com.quanutrition.app.selectiondialogs.DialogUtils
import com.quanutrition.app.selectiondialogs.MultipleSelectionModel
import com.quanutrition.app.selectiondialogs.SingleSelectionModel


class QuestionAdapter(private val list: ArrayList<QuestionModel>, private val mCtx: Context) : RecyclerView.Adapter<QuestionAdapter.MyViewHolder>() {

    lateinit var binding: QuestionListItemBinding


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var question: TextView
        var details: EditText
        var selection: EditText
        var choices : RecyclerView
        var onItemClicked : OnItemClicked?=null
        init {
            question = view.findViewById(R.id.question)
            details = view.findViewById(R.id.details)
            selection = view.findViewById(R.id.selection)
            choices = view.findViewById(R.id.choices)

            details.addTextChangedListener(object : TextWatcher{
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    onItemClicked?.onClick(s.toString(),adapterPosition)
                }

            })
        }
    }

    interface OnItemClicked {
        fun onClick(name: String?,pos:Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.question_list_item, parent, false)

//        binding = QuestionListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
//        val itemView = binding.root
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = list[position]

        holder.question.text = model.question
//        holder.details.setText("")
        Log.d("Model text", model.text)
        holder.details.setText(model.text)


        holder.onItemClicked = object : OnItemClicked {
            override fun onClick(name: String?, pos: Int) {
                list[pos].text = name!!
            }
        }

        /*holder.details.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                model.text = s.toString()
//                holder.details.setText(model.text)

                Log.d("Model text", model.text)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })*/

        if (model.multiple == 0) {
            if (model.choices.size <= 4) {
                holder.choices.visibility = View.VISIBLE
                holder.selection.visibility = View.GONE

                val adapter = RadioChoiceAdapter(model.choices,mCtx)
                holder.choices.layoutManager = GridLayoutManager(mCtx,2)
                holder.choices.adapter = adapter

                adapter.setOnItemClicked { pos -> run{
                    for (i in 0 until model.choices.size) {
                        if (i != pos) {
                            model.choices[i].selected = 0
                        }else{
                            model.choices[i].selected = 1
                        }
                    }

                    holder.choices.post(Runnable { adapter.notifyDataSetChanged() })

//                    adapter.notifyDataSetChanged()
                }  }

            } else {
                holder.choices.visibility = View.GONE
                holder.selection.visibility = View.VISIBLE
            holder.selection.setText("")
                holder.selection.isFocusable = false
                val list = ArrayList<SingleSelectionModel>()

                for (i in 0 until model.choices.size) {
                    val singleModel = SingleSelectionModel(model.choices[i].id, model.choices[i].`val`)
                    list.add(singleModel)
                    if (model.choices[i].selected == 1)
                        holder.selection.setText(model.choices[i].`val`)
                }

                holder.selection.setOnClickListener {
                    DialogUtils.getSingleSearchDialog(mCtx, list) { position, item ->
                        run {
                            holder.selection.setText(model.choices[position].`val`)
                            for (i in 0 until model.choices.size) {
                                if (i == position)
                                    model.choices[i].selected = 1
                                else
                                    model.choices[i].selected = 0
                            }
                        }
                    }
                }
            }
        } else {

            holder.selection.visibility = View.VISIBLE
            holder.choices.visibility = View.GONE
            holder.selection.isFocusable = false
            var list = ArrayList<MultipleSelectionModel>()
            var flag = 0
            for (i in 0 until model.choices.size) {
                var selected = false

                if (model.choices[i].selected == 1) {
                    selected = true
                    flag++
                }
                val multiModel = MultipleSelectionModel(model.choices[i].id, model.choices[i].`val`, selected)
                list.add(multiModel)
            }
            if (flag != 0)
                holder.selection.setText("$flag items selected")
            else
                holder.selection.setText("")

            holder.selection.setOnClickListener {
                DialogUtils.getMultipleSearchDialog(mCtx, list) { items ->
                    run {
                        list = items
                        var flag = 0
                        for (i in 0 until list.size) {
                            if (list[i].isSelected) {
                                flag++
                                model.choices[i].selected = 1
                            } else {
                                model.choices[i].selected = 0
                            }
                        }

                        if (flag == 0) {
                            holder.selection.setText("")
                        } else {
                            holder.selection.setText("$flag items selected")
                        }
                    }
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

}