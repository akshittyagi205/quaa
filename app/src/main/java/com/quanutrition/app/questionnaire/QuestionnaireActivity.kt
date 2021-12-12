package com.quanutrition.app.questionnaire

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.quanutrition.app.R
import com.quanutrition.app.Utils.NetworkManager
import com.quanutrition.app.Utils.OnClickListener
import com.quanutrition.app.Utils.Tools
import com.quanutrition.app.databinding.ActivityQuestionnaireBinding
import org.json.JSONException
import org.json.JSONObject

class QuestionnaireActivity : AppCompatActivity() {

    lateinit var binding: ActivityQuestionnaireBinding
    lateinit var list : ArrayList<QuestionCategoryModel>
    lateinit var adapter: QuestionCategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionnaireBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        list = ArrayList()

        /*list.add(QuestionCategoryModel("1","Eye", R.drawable.ic_globe))
        list.add(QuestionCategoryModel("1","Eye", R.drawable.ic_globe))
        list.add(QuestionCategoryModel("1","Eye", R.drawable.ic_globe))
        list.add(QuestionCategoryModel("1","Eye", R.drawable.ic_globe))
        list.add(QuestionCategoryModel("1","Eye", R.drawable.ic_globe))
        list.add(QuestionCategoryModel("1","Eye", R.drawable.ic_globe))
        list.add(QuestionCategoryModel("1","Eye", R.drawable.ic_globe))
        list.add(QuestionCategoryModel("1","Eye", R.drawable.ic_globe))
        list.add(QuestionCategoryModel("1","Eye", R.drawable.ic_globe))*/


        adapter = QuestionCategoryAdapter(list,this)
        binding.re.layoutManager = LinearLayoutManager(this)
        binding.re.adapter = adapter

        adapter.setOnClickListener(OnClickListener { pos -> run{
            val model = list[pos]
            val intent = Intent(this@QuestionnaireActivity,QuestionAnswerActivity::class.java)
            intent.putExtra("category",model.name)
            intent.putExtra("id",model.id)
            startActivity(intent)
        }
        })

        fetchData()
    }

    fun fetchData(){
        val ad = Tools.getDialog("Loading...",this)
        ad.show()

        val listener =  Response.Listener<String> { response ->run{
            ad.dismiss()
            try{
                val res = JSONObject(response)
                Log.d("response",response)
                if(res.getInt("res")==1){

                    val data = res.getJSONArray("data")
                    for(i in 0 until data.length()){
                        val model = QuestionCategoryModel(i.toString(),data.getString(i),0)
                        when(data.getString(i).toUpperCase()){
                            "HEAD" -> model.imageId = R.drawable.ic_head
                            "EYES" -> model.imageId = R.drawable.ic_eye
                            "EARS" -> model.imageId = R.drawable.ic_ear
                            "NOSE" -> model.imageId = R.drawable.ic_nose
                            "TEETH" -> model.imageId = R.drawable.ic_teeth
                            "FACIAL SKIN" -> model.imageId = R.drawable.ic_face
                            "THROAT" -> model.imageId = R.drawable.ic_throat
                            "THYROID" -> model.imageId = R.drawable.ic_thyroid
                            "LUNGS" -> model.imageId = R.drawable.ic_lungs
                            "HEART" -> model.imageId = R.drawable.ic_heart
                            "STOMACH" -> model.imageId = R.drawable.ic_stomach
                            "INTESTINES" -> model.imageId = R.drawable.ic_intestine
                            "KIDNEY" -> model.imageId = R.drawable.ic_kidney
                            "SKIN" -> model.imageId = R.drawable.ic_skin
                            "JOINTS" -> model.imageId = R.drawable.ic_joints
                            "MUSCLES" -> model.imageId = R.drawable.ic_muscle
                            "REPRODUCTIVE ORGANS" -> model.imageId = R.drawable.ic_reproductive
                            "CHILDREN" -> model.imageId = R.drawable.ic_children
                            "ENERGY" -> model.imageId = R.drawable.ic_energy
                            "SLEEP" -> model.imageId = R.drawable.ic_sleep
                        }
//                        model.imageId = R.drawable.ic_globe
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
            Tools.initNetworkErrorToast(this@QuestionnaireActivity)
        }}

        NetworkManager.getInstance(this).sendGetRequest(Urls.getCategory,listener,errorListener,this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == android.R.id.home){
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}
