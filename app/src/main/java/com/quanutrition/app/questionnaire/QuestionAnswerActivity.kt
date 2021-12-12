package com.quanutrition.app.questionnaire

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.quanutrition.app.R
import com.quanutrition.app.Utils.NetworkManager
import com.quanutrition.app.Utils.Tools
import com.quanutrition.app.databinding.ActivityQuestionAnswerBinding
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class QuestionAnswerActivity : AppCompatActivity() {

    lateinit var binding : ActivityQuestionAnswerBinding
    lateinit var list: ArrayList<QuestionModel>
    lateinit var adapter : QuestionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionAnswerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title = intent?.getStringExtra("category").toString()

        list = ArrayList()

//        loadStaticData()

        adapter = QuestionAdapter(list,this)
        binding.re.layoutManager = LinearLayoutManager(this)
        binding.re.adapter = adapter

        fetchData()

        binding.save.setOnClickListener {
            prepareSave()
        }
    }

    private fun prepareSave() {
        val data = JSONArray()

        for(i in 0 until list.size){

            val ob = JSONObject()
            val model = list[i]
            ob.put("ques_id",model.id)
            ob.put("ques_name",model.name)
            ob.put("question",model.question)
            ob.put("text",model.text)
            ob.put("text_name","")

            val choices = JSONArray()
            for(j in 0 until model.choices.size){

                val choice = model.choices[j]
                if(choice.selected==1){

                    val choiceOb = JSONObject()
                    choiceOb.put("id",choice.id)
                    choiceOb.put("val",choice.`val`)
                    choices.put(choiceOb)
                }

            }

            ob.put("choices",choices)
            data.put(ob)
        }

        sendSaveRequest(data)
    }

    private fun sendSaveRequest(data: JSONArray) {

        val ad = Tools.getDialog("Saving...",this)
        ad.show()

        val params = JSONObject()
        params.put("data",data)
        params.put("category",intent?.getStringExtra("category").toString())

        Log.d("params",params.toString())

        val listener = Response.Listener<String> { response -> run{

            ad.dismiss()
            try{
                Log.d("Response",response)
                val res = JSONObject(response)
                if(res.getInt("res")==1){

                    Tools.initCustomToast(this@QuestionAnswerActivity,"Data Saved!")
                    finish()

                }else{
                    Tools.initNetworkErrorToast(this@QuestionAnswerActivity)
                }
            }catch (e : JSONException){
                e.printStackTrace()
            }

        }  }

        val errorListener = Response.ErrorListener {
            ad.dismiss()
            Tools.initNetworkErrorToast(this)
        }

        NetworkManager.getInstance(this).sendRawPOstRequest(Urls.saveQuestions,params.toString(),listener,errorListener,this)

    }

    fun fetchData(){
        val ad = Tools.getDialog("Loading...",this)
        ad.show()

        val listener =  Response.Listener<String> { response ->run{
            ad.dismiss()
            try{
                val res = JSONObject(response)
                Log.d("Response",response)
                if(res.getInt("res")==1){
                    val data = res.getJSONArray("data")
                    for(i in 0 until data.length()){

                        val ob = data.getJSONObject(i)
                        val choices = ArrayList<QuestionModel.ChoiceModel>()

                        val choiceArray = ob.getJSONArray("choices")
                        for(j in 0 until choiceArray.length()){
                            val ob1 = choiceArray.getJSONObject(j)
                            val model1 = QuestionModel.ChoiceModel(ob1.getString("id"),ob1.getString("val"),ob1.getInt("is_selected"))
                            choices.add(model1)
                        }

                        val model = QuestionModel(ob.getString("ques_id"),ob.getString("ques_name"),ob.getString("question"),ob.getString("text"),"",ob.getInt("is_multiple"),choices)
                        list.add(model)

                    }
                    adapter.notifyDataSetChanged()
                }

            }catch (e : JSONException){
                e.printStackTrace()
            }
        } }

        val errorListener = Response.ErrorListener { _ -> run{
            ad.dismiss()
            Tools.initNetworkErrorToast(this@QuestionAnswerActivity)
        }}

        NetworkManager.getInstance(this).sendGetRequest("${Urls.getQuestions}?category=${intent.getStringExtra("category")}",listener,errorListener,this)
    }

    private fun loadStaticData() {
        val choice1 = QuestionModel.ChoiceModel("yes1","Yes",0)
        val choice2 = QuestionModel.ChoiceModel("yes1","No",0)
        val choice3 = QuestionModel.ChoiceModel("yes1","Maybe",0)
        val choice4 = QuestionModel.ChoiceModel("yes1","Sometimes",0)
        val choice5 = QuestionModel.ChoiceModel("yes1","Never",0)

        val choices1 = ArrayList<QuestionModel.ChoiceModel>()
        choices1.add(choice1)
        choices1.add(choice2)

        val choices2 = ArrayList<QuestionModel.ChoiceModel>()
        choices2.add(choice1)
        choices2.add(choice2)
        choices2.add(choice3)

        val choices3 = ArrayList<QuestionModel.ChoiceModel>()
        choices3.add(choice1)
        choices3.add(choice2)
        choices3.add(choice3)
        choices3.add(choice4)
        choices3.add(choice5)

        val model1 = QuestionModel("id1","name1","This is a sample ques1?","","test1",0,choices1)
        val model2 = QuestionModel("id2","name2","This is a sample ques2?","","test2",0,choices2)
        val model3 = QuestionModel("id3","name3","This is a sample ques3?","","test3",0,choices3)
        val model4 = QuestionModel("id4","name4","This is a sample ques4?","","test4",1,choices2)

        list.add(model1)
        list.add(model2)
        list.add(model3)
        list.add(model4)
        list.add(model1)
        list.add(model2)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == android.R.id.home){
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}
