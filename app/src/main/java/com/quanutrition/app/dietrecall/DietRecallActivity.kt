package com.quanutrition.app.dietrecall

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.quanutrition.app.R
import com.quanutrition.app.Utils.NetworkManager
import com.quanutrition.app.Utils.Tools
import com.quanutrition.app.databinding.ActivityDietRecallBinding
import com.quanutrition.app.selectiondialogs.DialogUtils
import com.quanutrition.app.selectiondialogs.SingleSelectionModel
import kotlinx.android.synthetic.main.activity_diet_recall.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class DietRecallActivity : AppCompatActivity() {

    lateinit var binding : ActivityDietRecallBinding
    lateinit var mealList : ArrayList<DietRecallModel>
    lateinit var adapter: DietRecallAdapter
    lateinit var mealNames : ArrayList<SingleSelectionModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDietRecallBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        mealList = ArrayList()
        mealNames = ArrayList()

        adapter = DietRecallAdapter(mealList,this)
        binding.re.layoutManager = LinearLayoutManager(this)
        binding.re.adapter = adapter

        adapter.setOnClickListener { pos -> run{
            val foodList = ArrayList<FoodModel>()
            foodList.add(FoodModel("",""))
            mealList.add(pos+1,DietRecallModel(mealList[pos].mealname,mealList[pos].mealTime,foodList))
            adapter.notifyDataSetChanged()
            binding.re.scrollToPosition(pos+1)
        } }

        adapter.setOnMealNameSelected { pos -> run{

            DialogUtils.getSingleSearchDialog(this@DietRecallActivity,mealNames){i,model ->run{
                mealList[pos].mealname = model.label
                adapter.notifyItemChanged(pos)
            }}

        } }

        fetchData()

        binding.save.setOnClickListener {
            prepareSaveRecall()
        }
    }

    private fun prepareSaveRecall() {

        val recallAr = JSONArray()
        try{
            for(i in 0 until mealList.size){

                val ob = JSONObject()
                val ar = JSONArray()

                val model = mealList[i]
                if(model.foodList.size>0) {
                    ob.put("mealname", model.mealname)
                    ob.put("time", model.mealTime)

                    for(j in 0 until model.foodList.size){
                        val dataOb = JSONObject()
                        dataOb.put("foodname",model.foodList[j].food)
                        dataOb.put("quantity",model.foodList[j].quantity)
                        ar.put(dataOb)
                    }

                    ob.put("fooddata",ar)
                    recallAr.put(ob)
                }
            }

            saveData(recallAr)

        }catch (e : JSONException){
            e.printStackTrace()
        }

    }

    private fun saveData(recallAr : JSONArray) {

        val ad = Tools.getDialog("Saving Data...",this)
        ad.show()

        val params = JSONObject()
        try {
            params.put("data", recallAr)
            params.put("weekend", binding.weekend.text.toString())
            params.put("followup", binding.followup.text.toString())
        }catch (e : JSONException){
            e.printStackTrace()
        }

        val listener = Response.Listener<String?> { response -> run{
            ad.dismiss()
            try{
                Log.d("Response",response)
                val res = JSONObject(response)
                if(res.getInt("res")==1){
                    Tools.initCustomToast(this@DietRecallActivity,"24 Hour Diet Recall Saved!")
                    finish()
                }
            }catch (e : JSONException){
                e.printStackTrace()
            }
        }
        }

        val errorListener = Response.ErrorListener {
            ad.dismiss()
            Tools.initNetworkErrorToast(this)
        }

        NetworkManager.getInstance(this).sendRawPOstRequest(Urls.save24Hr,params.toString(),listener,errorListener,this)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.diet_recall_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.addMeal){
            val foodList = ArrayList<FoodModel>()
            foodList.add(FoodModel("",""))
            if(mealNames.isEmpty())
                mealList.add(DietRecallModel("Breakfast","",foodList))
            else
                mealList.add(DietRecallModel(mealNames[0].label,"",foodList))
            adapter.notifyDataSetChanged()
            binding.re.scrollToPosition(mealList.size-1)
        }

        if(item.itemId == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun fetchData(){
        val ad = Tools.getDialog("Loading...",this)
        ad.show()

        val listener = Response.Listener<String?> { response ->
            run{
                ad.dismiss()
                try{
                    Log.d("Response",response);
                    val res = JSONObject(response)
                    if(res.getInt("res")==1){

                        val data = res.getJSONObject("data")

                        val meals = data.getJSONArray("meals")
                        for(i in 0 until meals.length()){
                            mealNames.add(SingleSelectionModel(i.toString(),meals.getString(i)))
                        }

                        val recallData = data.getJSONArray("recall")

                        mealList.clear()

                        if(recallData.length()>0){

                            for(i in 0 until recallData.length()){
                                val ob = recallData.getJSONObject(i)
                                val foodList = ArrayList<FoodModel>()
                                val foodAr = ob.getJSONArray("fooddata")
                                for(j in 0 until foodAr.length()){

                                    val ob1 = foodAr.getJSONObject(j)
                                    val foodModel = FoodModel(ob1.getString("foodname"),ob1.getString("quantity"))
                                    foodList.add(foodModel)
                                }

                                val mealModel = DietRecallModel(ob.getString("mealname"),ob.getString("time"),foodList)
                                mealList.add(mealModel)
                            }

                        }else{
                            val model = FoodModel("","")
                            val foodList = ArrayList<FoodModel>()
                            foodList.add(model)
                            if(mealNames.size>0)
                                mealList.add(DietRecallModel(mealNames[0].label,"",foodList))
                            else
                                mealList.add(DietRecallModel("Breakfast","",foodList))
                        }

                        adapter.notifyDataSetChanged()

                    }
                }catch (e : JSONException){
                    e.printStackTrace()
                }
            }
        }
        val errorListener = Response.ErrorListener {
            ad.dismiss()
            Tools.initNetworkErrorToast(this)
            val model = FoodModel("","")
            val foodList = ArrayList<FoodModel>()
            foodList.add(model)

            mealList.add(DietRecallModel("Breakfast","",foodList))
            adapter.notifyDataSetChanged()
        }


        NetworkManager.getInstance(this).sendGetRequest(Urls.get24Hr,listener,errorListener,this)
    }
}
